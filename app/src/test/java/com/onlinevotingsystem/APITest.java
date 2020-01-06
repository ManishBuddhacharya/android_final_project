package com.onlinevotingsystem;

import com.onlinevotingsystem.BLL.LoginSignupBLL;
import com.onlinevotingsystem.BLL.PositionBLL;

import junit.framework.TestCase;

import org.junit.Test;


public class APITest extends TestCase {

    @Test
    public void testLogin(){
        LoginSignupBLL bll = new LoginSignupBLL("kirito", "kirito");
        int res = bll.checkUser().size();
        assertEquals(3, res);
    }

    @Test
    public void testLoginFail(){
        LoginSignupBLL bll = new LoginSignupBLL("kiritoo", "kirito");
        int res = bll.checkUser().size();
        assertEquals(0, res);
    }

    @Test
    public void testPostionAdd(){
        PositionBLL bll = new PositionBLL("position new one");
        boolean res = bll.addPosition("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Imtpcml0byIsImlhdCI6MTU2MjQxMzM4N30.GrZVtZpBHCq-hsn08-ZuAU7UFZ4zmiCXQPeFX4-NmEM");
        assertEquals(true, res);
    }

    @Test
    public void testPostionUpdate(){
        PositionBLL bll = new PositionBLL("position old one");
        boolean res = bll.updatePosition("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Imtpcml0byIsImlhdCI6MTU2MjQxMzM4N30.GrZVtZpBHCq-hsn08-ZuAU7UFZ4zmiCXQPeFX4-NmEM",25);
        assertEquals(true, res);
    }

    @Test
    public void testMemberAdd(){
        LoginSignupBLL bll = new LoginSignupBLL("test1","test1","test1","test1","test1","test1","test1");
        boolean res = bll.insertUser();
        assertEquals(true, res);
    }

    @Test
    public void testMemberUpdate(){
        LoginSignupBLL bll = new LoginSignupBLL("test12","test12","test12","test12","test12");
        boolean res = bll.updateUser("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Imtpcml0byIsImlhdCI6MTU2MjQxMzM4N30.GrZVtZpBHCq-hsn08-ZuAU7UFZ4zmiCXQPeFX4-NmEM",32);
        assertEquals(true, res);
    }

}
