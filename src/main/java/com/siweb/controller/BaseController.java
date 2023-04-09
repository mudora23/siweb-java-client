package com.siweb.controller;

import com.siweb.controller.utility.UtilityHttpController;
import com.siweb.model.UserModel;

/***
 * BaseController is the base for all pages
 */
public abstract class BaseController {

    protected final UtilityHttpController http = UtilityHttpController.getInstance();
    protected final UserModel userModel = UserModel.getInstance();

}
