package pl.com.bottega.cymes.commons.rest;

import java.util.List;

record Errors(List<Error> errors) {
}

record Error(String field, String message) {

}

record GlobalError(String error) {}