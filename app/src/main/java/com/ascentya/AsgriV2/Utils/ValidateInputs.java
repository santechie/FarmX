package com.ascentya.AsgriV2.Utils;


import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ValidateInputs class has different static methods, to validate different types of user Inputs
 **/

public class ValidateInputs {


    //*********** Validate Email Address ********//

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    //*********** Validate Name Input ********//

    public static boolean isValidName(String name) {
        String regExpn = "^([a-zA-Z ]{1,24})+$";
        CharSequence inputStr = name;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }


    //*********** Validate User Login ********//

    public static boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }


    //*********** Validate Password Input ********//

    public static boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_$@$!%*?&]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }


    //*********** Validate Phone Number********//

    public static boolean isValidPhoneNo(String phoneNo) {
        return !TextUtils.isEmpty(phoneNo) && Patterns.PHONE.matcher(phoneNo).matches();
    }


    //*********** Validate Number Input ********//

    public static boolean isValidNumber(String number) {

        String regExpn = "^[0-9]{1,24}$";
        CharSequence inputStr = number;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches() && number.length() == 10;
    }


    //*********** Validate Any Input ********//

    public static boolean isValidInput(String input) {

        if (input == null)
            return false;

        String regExpn = "(.*?)?((?:[a-z][a-z]+))";
        CharSequence inputStr = input;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return !input.equalsIgnoreCase("");
    }


    //*********** Validate Search Query ********//

    public static boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static boolean isValidIFSC(String ifsc){
        String reg = "^[A-Z]{4}0[A-Z0-9]{6}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    public static boolean isValidGST(String gst) {
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gst);
        return matcher.matches();
    }

    public static boolean isValidPincode(String stringFrom) {
        String regex = "^[1-9][0-9]{5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringFrom);
        return matcher.matches();
    }

    public static boolean isValidOtp(String otp) {
        String regex = "^[0-9]{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(otp);
        return matcher.matches();
    }
}

