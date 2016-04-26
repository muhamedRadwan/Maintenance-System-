/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SE;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Date;

/**
 *
 * @author moroclash
 */
public  class Validations {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String Password_PATTERN ="^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?=\\S+$).{6,20})"; 
    /*
            (?=.*\d)		#   must contains one digit from 0-9
            (?=.*[a-z])		#   must contains one lowercase characters
            (?=.*[A-Z])		#   must contains one uppercase characters
            (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
            .		#     match anything with previous condition checking
            {6,20}	#        length at least 6 characters and maximum of 20
       */
    private static final String Date_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\\\d\\\\d)";
    /*
    (			#start of group #1
 0?[1-9]		#  01-09 or 1-9
 |                  	#  ..or
 [12][0-9]		#  10-19 or 20-29
 |			#  ..or
 3[01]			#  30, 31
) 			#end of group #1
  /			#  follow by a "/"
   (			#    start of group #2
    0?[1-9]		#	01-09 or 1-9
    |			#	..or
    1[012]		#	10,11,12
    )			#    end of group #2
     /			#	follow by a "/"
      (			#	  start of group #3
       (19|20)\\d\\d	#	    19[0-9][0-9] or 20[0-9][0-9]
       )		#	  end of group #3
    */
    private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    
    
    public static Pattern Email_compile() {
          return pattern = Pattern.compile(EMAIL_PATTERN);
	}
    public static Pattern Date_compile() {
          return pattern = Pattern.compile(Date_PATTERN);
	}
    public static Pattern Password_compile() {
          return pattern = Pattern.compile(Password_PATTERN);
	}
   public static boolean Is_digit(String Word)
   {
      try{
          Integer.parseInt(Word);
          return true;
      }catch(Exception e){
          return false;
      }
              
       
       
   }
   
   public static boolean Is_alpha(String Word)
   {
       int len=Word.length();
       while (len!=0)
       {
            char s=Word.charAt(len); 
            boolean tr=  Character.isDigit(s);
                if(tr==true)
                   return false;
          len --;
       }
       return true;
   }
   
   public static boolean Is_email(String Word)
   {    //anything@3digit at least .com  
       pattern= Email_compile();
       matcher = pattern.matcher(Word);
		return matcher.matches();
   }
   
   
   public static boolean Is_passord(String Word)
   { //9 digit
       
        pattern= Password_compile();
       matcher = pattern.matcher(Word);
		return matcher.matches();
   }
   
   public static boolean Is_date(String Word)
   {
       //1920-02-30 OR 30-02-1964
       pattern= Date_compile();
       matcher = pattern.matcher(Word);
		return matcher.matches();
   }
   
   public static boolean Is_double(String Word)
   {
       try{
           Double.parseDouble(Word);
           return true;
       }catch(Exception e){
           return false;
       }
   }
    
}
