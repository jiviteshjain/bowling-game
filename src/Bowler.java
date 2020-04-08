/*
 * Bowler.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log: Bowler.java,v $
 *     Revision 1.3  2003/01/15 02:57:40  ???
 *     Added accessors and and equals() method
 *
 *     Revision 1.2  2003/01/12 22:23:32  ???
 *     *** empty log message ***
 *
 *     Revision 1.1  2003/01/12 19:09:12  ???
 *     Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 *  Class that holds all bowler info
 *
 */

public class Bowler {

    private String fullName;
    private String nickName;
    private String email;

    public Bowler( String nick, String full, String mail ) {
	nickName = nick;
	fullName = full;
  	email = mail;
    }


    public String getNickName() {

        return nickName;  

    }
	public boolean checkvalue_1(boolean retval, String temp){
		if(!(nickName.equals(temp))){
			retval=false	;
		}
		return retval;
	}
	public boolean checkvalue_2(boolean retval,String temp){
		if(!(fullName.equals(temp))){
			retval=false	;
		}
		return retval;
	}
	public boolean checkvalue_3(boolean retval,String temp){
		if(!(email.equals(temp))){
			retval=false	;
		}
		return retval;
	}
	public String getFullName ( ) {
			return fullName;
	}
	
	public String getNick ( ) {
		return nickName;
	}

	public String getEmail ( ) {
		return email;	
	}
	
	public boolean equals ( Bowler b) {
		boolean retval = true;
		retval=checkvalue_1(retval,b.getNickName());
		retval=checkvalue_2(retval,b.getFullName());
		retval=checkvalue_3(retval,b.getEmail());
		return retval;
	}
}