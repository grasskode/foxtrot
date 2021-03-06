/**
 * Copyright 2014 Flipkart Internet Pvt. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.foxtrot.common.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flipkart.foxtrot.common.query.general.AnyFilter;
import com.flipkart.foxtrot.common.query.general.EqualsFilter;
import com.flipkart.foxtrot.common.query.general.NotEqualsFilter;
import com.flipkart.foxtrot.common.query.numeric.*;
import com.flipkart.foxtrot.common.query.string.ContainsFilter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * User: Santanu Sinha (santanu.sinha@flipkart.com)
 * Date: 14/03/14
 * Time: 2:09 AM
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "operator")
@JsonSubTypes({
        //Numeric
        @JsonSubTypes.Type(value = GreaterEqualFilter.class, name = FilterOperator.greater_equal),
        @JsonSubTypes.Type(value = GreaterThanFilter.class, name = FilterOperator.greater_than),
        @JsonSubTypes.Type(value = LessEqualFilter.class, name = FilterOperator.less_equal),
        @JsonSubTypes.Type(value = LessThanFilter.class, name = FilterOperator.less_than),
        @JsonSubTypes.Type(value = BetweenFilter.class, name = FilterOperator.between),

        //General
        @JsonSubTypes.Type(value = EqualsFilter.class, name = FilterOperator.equals),
        @JsonSubTypes.Type(value = NotEqualsFilter.class, name = FilterOperator.not_equals),
        @JsonSubTypes.Type(value = AnyFilter.class, name = FilterOperator.any),

        //String
        @JsonSubTypes.Type(value = ContainsFilter.class, name = FilterOperator.contains)
})

public abstract class Filter {
    @JsonIgnore
    private final String operator;

    @NotNull
    private String field;

    protected Filter(String operator) {
        this.operator = operator;
    }

    protected Filter(String operator, String field) {
        this.operator = operator;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public abstract void accept(FilterVisitor visitor) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (!field.equals(filter.field)) return false;
        if (!operator.equals(filter.operator)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operator.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("operator", operator)
                .append("field", field)
                .toString();
    }
}
