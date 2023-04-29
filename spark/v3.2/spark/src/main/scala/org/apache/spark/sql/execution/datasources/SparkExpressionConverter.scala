/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.spark.sql.execution.datasources

import org.apache.iceberg.spark.SparkFilters
import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.plans.logical.Filter
import org.apache.spark.sql.catalyst.plans.logical.LeafNode
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

object SparkExpressionConverter {

  private def convertToIcebergExpression(sparkExpression: Expression): org.apache.iceberg.expressions.Expression = {
    // Currently, it is a double conversion as we are converting Spark expression to Spark filter
    // and then converting Spark filter to Iceberg expression.
    // But these two conversions already exist and well tested. So, we are going with this approach.
    SparkFilters.convert(DataSourceStrategy.translateFilter(sparkExpression, supportNestedPredicatePushdown = true).get)
  }

  @throws[AnalysisException]
  private def collectOptimizedLogicalPlan(session: SparkSession, tableName: String, where: String): LogicalPlan = {
    val tableAttrs = session.table(tableName).queryExecution.analyzed.output
    val unresolvedExpression = session.sessionState.sqlParser.parseExpression(where)
    val filter = Filter(unresolvedExpression, DummyRelation(tableAttrs))
    session.sessionState.executePlan(filter).optimizedPlan
  }

  @throws[AnalysisException]
  def collectResolvedIcebergExpression(session: SparkSession,
                                       tableName: String, where: String): org.apache.iceberg.expressions.Expression = {
    val optimizedLogicalPlan = collectOptimizedLogicalPlan(session, tableName, where)
    if (optimizedLogicalPlan.containsChild.nonEmpty) {
      val sparkExpressionOption = optimizedLogicalPlan.collectFirst {
        case filter: Filter => filter.condition
      }
      convertToIcebergExpression(sparkExpressionOption.get)
    } else {
      val tableAttrs = session.table(tableName).queryExecution.analyzed.output
      val firstColumn = tableAttrs.head
      val whereExpression = s"$firstColumn is null and $where"
      val optimizedLogicalPlan = collectOptimizedLogicalPlan(session, tableName, whereExpression)
      if (optimizedLogicalPlan.containsChild.isEmpty) { org.apache.iceberg.expressions.Expressions.alwaysFalse() }
      else { org.apache.iceberg.expressions.Expressions.alwaysTrue() }
    }
  }

  private case class DummyRelation(output: Seq[Attribute]) extends LeafNode
}
