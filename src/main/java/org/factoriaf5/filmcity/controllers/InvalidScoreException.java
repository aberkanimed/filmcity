package org.factoriaf5.filmcity.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Score Value")
public class InvalidScoreException extends RuntimeException{
}