/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.expenses.shared;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.sample.expenses.server.domain.Employee;

import java.util.List;

/**
 * Builds requests for the Employee service.
 */
@Service(Employee.class)
public interface EmployeeRequest extends RequestContext {

  /**
   * @return a request object
   */
  Request<Long> countEmployees();

  /**
   * @return a request object
   */
  Request<Long> countEmployeesByDepartment(
      String department);

  /**
   * @return a request object
   */
  Request<List<EmployeeProxy>> findAllEmployees();

  /**
   * @return a request object
   */
  Request<EmployeeProxy> findEmployee(Long id);

  /**
   * @return a request object
   */
  Request<List<EmployeeProxy>> findEmployeeEntries(int firstResult,
      int maxResults);

  /**
   * @return a request object
   */
  Request<List<EmployeeProxy>> findEmployeeEntriesByDepartment(
      String department, int firstResult, int maxResults);

  /**
   * @return a request object
   */
  InstanceRequest<EmployeeProxy, Void> persist();

 /**
  * @return a request object
  */
  InstanceRequest<EmployeeProxy, Void> remove();
}
