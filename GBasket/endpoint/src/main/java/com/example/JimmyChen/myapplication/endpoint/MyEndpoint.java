/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.JimmyChen.myapplication.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "endpoint.myapplication.JimmyChen.example.com",
    ownerName = "endpoint.myapplication.JimmyChen.example.com",
    packagePath=""
  )
)
public class MyEndpoint {

    public static List<MyBean> user = new ArrayList<>();

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name="adduser")
    public MyBean adduser(@Named("email") String email, @Named("pw") String pw) throws NotFoundException {

        //Check for already exists
        int exist = user.indexOf(new MyBean(email));
        if (exist != -1) throw new NotFoundException("Quote Record already exists");

        MyBean q = new MyBean(email, pw);
        user.add(q);
        return q;
    }


}

