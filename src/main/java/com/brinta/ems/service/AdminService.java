package com.brinta.ems.service;

import com.brinta.ems.dto.TokenPair;
import com.brinta.ems.entity.Admin;
import com.brinta.ems.exception.exceptionHandler.DuplicateEntryException;
import com.brinta.ems.request.registerRequest.LoginRequest;
import com.brinta.ems.request.registerRequest.RegisterAdminRequest;


public interface AdminService {

    Admin registerAdmin(RegisterAdminRequest request) throws DuplicateEntryException;
    TokenPair loginAdmin(LoginRequest request);
}
