/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-03-25 20:06:55 UTC)
 * on 2016-04-04 at 22:29:02 UTC 
 * Modify at your own risk.
 */

package com.example.jimmychen.myapplication.endpoint.myApi.model;

/**
 * Model definition for MyBean.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the myApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MyBean extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String data;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String fname;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lname;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getData() {
    return data;
  }

  /**
   * @param data data or {@code null} for none
   */
  public MyBean setData(java.lang.String data) {
    this.data = data;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public MyBean setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFname() {
    return fname;
  }

  /**
   * @param fname fname or {@code null} for none
   */
  public MyBean setFname(java.lang.String fname) {
    this.fname = fname;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLname() {
    return lname;
  }

  /**
   * @param lname lname or {@code null} for none
   */
  public MyBean setLname(java.lang.String lname) {
    this.lname = lname;
    return this;
  }

  @Override
  public MyBean set(String fieldName, Object value) {
    return (MyBean) super.set(fieldName, value);
  }

  @Override
  public MyBean clone() {
    return (MyBean) super.clone();
  }

}
