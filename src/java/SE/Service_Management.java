/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SE;

import Data_access.DB_controller;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;

/**
 *
 * @author emad
 */
public class Service_Management {

    private static Service_Management manage = null;
    private ArrayList<Request> Request_buffer;
    private ArrayList<Complain> Complain_buffer;

    private Service_Management() {
  }

    public static Service_Management Get_Serive_Management() {
        if (manage == null) {
            return new Service_Management();
        }
        return null;
    }

    
    //sala7
    public ArrayList<Integer> Get_Technical (int order_id)
    {
       ArrayList<Integer> tech = new ArrayList<>();
       DB_controller DB=DB_controller.Get_DB_controller();
       DB.Connect();
     
       int request_id =-1; 
       int device_request  = -1;
       int Technical_id = -1;
       try {
           ResultSet result ;
           result = DB.Select(" Requist_id ", " order_fixable ", " Order_fixable_id = " + order_id);
            while(result.next())
            {
               request_id = result.getInt("Requist_id");
             
            }
           ResultSet result1 ;
           result1 = DB.Select("Device_of_this_request_id", "device_of_this_request", "Request_id = " + request_id);
           while(result1.next())
           {
             device_request = result1.getInt("Device_of_this_request_id");
            
           
           ResultSet result2 ;
           result2 = DB.Select("Technical_id", "order_flixer", "Device_of_this_request = " + device_request);
           while(result2.next())
           {
              Technical_id = result2.getInt("Technical_id");
              
              tech.add(Technical_id);
           }
           }
           return tech;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
           
            return null;
        }
    }
    

     //Sala7

    public void Return_order(int Order_id, String Technical_description) {

         DB_controller DB=DB_controller.Get_DB_controller();
         DB.Connect();
         DB.Update("order_fixable ", " State_id = 3 ," +  "Technical_description = '" + Technical_description + "'", " Order_fixable_id = " + Order_id);

         DB.Close();
    }
     //sala7
      public Order Search_order(int Order_id)
      {
         DB_controller DB=DB_controller.Get_DB_controller();
         DB.Connect();
         ResultSet result ;
         Order order = new Order();
         
         result = DB.Select(" * ", " order_fixable ", " Order_fixable_id = " + Order_id);
        try {
            while(result.next())
            {
              order.setId(result.getInt("Order_fixable_id"));
              order.setMy_requist_id(result.getInt("Requist_id"));
              order.setDate_start_id(result.getInt("Date_start_id"));
              order.setTecnical_description(result.getInt("Technical_description"));
              order.setDate_end_id(result.getInt("recept_Date_id"));
              order.setMy_service_id(result.getInt("Service_id"));
              order.setDate_start_id(result.getInt("State_id"));
              ArrayList <Integer> container = Get_Technical(Order_id);
              order.setMy_Technical_id(container);
            }
           
        } catch (SQLException ex) {
            ex.printStackTrace();
           DB.Close();
            return null;
        }
            DB.Close();
            return order;
      }
    

    //Emad 
    public void Add_Request(Request request) {
        this.Request_buffer = new ArrayList<Request>();
        DB_controller DB = DB_controller.Get_DB_controller();
        DB.Connect();
        HashMap<String, String> H = new HashMap<String, String>();
        H.put("Request_ID", Integer.toString(request.getID()));
        H.put("Date_ID", Integer.toString(request.getDate_id()));
        H.put("State_ID", Integer.toString(request.getState_id()));
        H.put("User_ID", Integer.toString(request.getUser_id()));
        DB.Insert("Request", H);
        DB.Close();
        Request_buffer.add(request);
    }

    
    
    //omar
    public void Add_Complain(Complain complain) {
        DB_controller DB = DB_controller.Get_DB_controller();
        System_manage sy = System_manage.Get_System_manage();
        this.Complain_buffer.add(complain);
        DB.Connect();
        Order or = Search_order(complain.getMy_order_id());
        Request rq = Search_requist(or.getMy_requist_id());
        HashMap<String,String> H = new HashMap<>();
        H.put("sender_id", Integer.toString(rq.getUser_id()));
        H.put("Date_id", Integer.toString(sy.Get_date_iD()));
        H.put("	Time", sy.Get_time());
        H.put("Content", complain.getContent());
        H.put("Type_id", "1");
        H.put("Parent_id", "0");
        int id = DB.Insert("message", H);
        H.clear();
        H.put("Message_id", Integer.toString(id));
        H.put("Order_id", Integer.toString(complain.getMy_order_id()));
        DB.Insert("complains_order", H);
    }
    
    
    
    
    //sala7
     public ArrayList<String> get_branch_phones( int branch_id ) {
         
         ArrayList <String> phone = new ArrayList <String>();
         DB_controller DB = DB_controller.Get_DB_controller();
         DB.Connect();
         
         ResultSet result = null;
         result = DB.Select(" phone ", " branch_phone ", " Branch_id = " + branch_id);
         String Phone = "";
         try {
            while(result.next())
            {
               phone.add(result.getString("phone"));
            }
            return phone;
        } catch (SQLException ex) {
            ex.printStackTrace();
            DB.Close();
        }
        DB.Close();
        return null;
     }
    
    
    
    
    //omar 0_0
    public Complain Show_Complain(int Coplain_num_in_Bufer,int Reciver_id)
    {
        Complain com = this.Complain_buffer.get(Coplain_num_in_Bufer);
        this.Complain_buffer.remove(Coplain_num_in_Bufer);
        DB_controller Db = DB_controller.Get_DB_controller();
        Db.Connect();
        HashMap<String,String> H = new HashMap<>();
        H.put("Reciever_id", Integer.toString(Reciver_id));
        H.put("Message_id", Integer.toString(com.getId()));
        H.put("State_id", "4");
        Db.Insert("recieved", H);
        return com;
    }


    //Emad
    public void Load_Complain() {
        this.Complain_buffer = new ArrayList<Complain>();
        ArrayList<Integer> A = new ArrayList<Integer>();
        Message_Controller MC = Message_Controller.Get_Message_Controller();
        DB_controller DB = DB_controller.Get_DB_controller();
        int x = -1;
        try {
            ResultSet result = DB.Select("*", "complains_order", "1");
            while (result.next())
            {
                A.add(result.getInt("Message_ID"));
            }
            result = DB.Select("*", "recieved", "1");
            while (result.next())
            {
                A.remove(result.getInt("Message_ID"));
            }
            int ID;
            for (int i = 0; i < A.size(); i++)
            {
                result = DB.Select("*", "message", "Message_ID="+A.get(i));
                while(result.next())
                {
                    Complain C=new Complain();
                    ID=result.getInt("Message_ID");
                    C.setId(ID);
                    C.setDate_id(result.getInt("Date_ID"));
                    C.setMassage_type_id(result.getInt("Type_ID"));
                    C.setContent(result.getString("Content"));
                    C.setTime(result.getString("Time"));
                    C.setMy_Commint(MC.get_massage_commintes(result.getInt("Message_ID")));
                    result=DB.Select("*", "recieved", "Message_ID="+ID);
                    while(result.next())
                    {
                        C.setState(result.getInt("State"));
                        C.setReciver(result.getInt("Reciever_ID"));
                    }
                    Complain_buffer.add(C);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        DB.Close();
    }

    //Emad
    public void Load_Request() {
        this.Request_buffer = new ArrayList<Request>();
        ArrayList<Integer> H = new ArrayList<Integer>();
        DB_controller DB = DB_controller.Get_DB_controller();
        ResultSet result = DB.Select("*", "request", "1");
        ResultSet result2;
        int x = -1;
        try {
            while (result.next()) {
                Request R = new Request();
                x = result.getInt("Request_ID");
                R.setID(x);
                R.setState_id(result.getInt("State_id"));
                R.setDate_id(result.getInt("Date_ID"));
                R.setUser_id(result.getInt("User_ID"));
                result2 = DB.Select("*", "device_of_this_request", "Request_ID=" + x);
                while (result2.next()) {
                    H.add(result.getInt("Device_ID"));
                }
                R.setDevice_id(H);
                this.Request_buffer.add(R);
            }
        } catch (Exception E) {
            System.out.println("Error in load Request");
        }
        DB.Close();
    }

//Emad
    public ArrayList<Request> Show_requists(int State) {
        DB_controller DB = DB_controller.Get_DB_controller();
        DB.Connect();
        ResultSet result = DB.Select("*", "request", "State_id=" + State);
        ArrayList<Request> R = new ArrayList<Request>();
        try {
            while (result.next()) {
                Request re = new Request();
                re.setID(result.getInt("Request_id"));
                re.setState_id(result.getInt("State_id"));
                re.setDate_id(result.getInt("Date_id"));
                R.add(re);
            }
            return R;
        } catch (Exception E) {
            System.out.println("Error");
        }
        return null;
    }
    

    //omar 0_0
    private int address_helper(int num, String TableName, int address_id) {
        try {
            DB_controller Db = DB_controller.Get_DB_controller();
            Db.Connect();
            ResultSet res = Db.Select("*", TableName, "Address_id=" + address_id);
            while (res.next()) {
                if (num == 0) {
                    return res.getInt("Address_id");
                }
                return address_helper(num - 1, TableName, res.getInt("Parent_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(System_manage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    //omar 0_0
    private int Get_n_b(int x, int Requiest_address_id) {
        try {
            DB_controller Db = DB_controller.Get_DB_controller();
            Db.Connect();
            int cuntry = address_helper(x, "address", Requiest_address_id);
            ResultSet res = Db.Select("*", "branch", "1");
            int branch_cuntry = 0;
            while (res.next()) {
                branch_cuntry = address_helper(x, "address", res.getInt("Address_id"));
                if (cuntry == branch_cuntry) {
                    return res.getInt("Branch_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(System_manage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    //omar 0_0
    public int Get_near_branch(int Requiest_address_id) {
        int x = 2;
        int c = Get_n_b(x, Requiest_address_id);
        if (c == 0) {
            c = Get_n_b(x + 1, Requiest_address_id);
        }
        return c;
    }

 
   //Emad
   public Time_chooser Get_Three_Date(Request request,Time_chooser Chose)
   {
       ArrayList<String> Dates=new ArrayList<String>();
       String Date = "";
       String FDATE = "";
       int DayPlus=0;
       int OptionID=-1;
       int NumberOfEmployees=0;
       int Branch_ID=-1; 
       int NumberOfOrdersAtDay = 0;
       System_manage S = System_manage.Get_System_manage();
       int Date_ID = S.Get_date_iD();
       DB_controller DB = DB_controller.Get_DB_controller();
       DB.Connect();
       try 
       {
           Branch_ID=this.Get_near_branch(request.getAddress_ID());
           OptionID=S.Search_User_OptionByName("Branch_ID");
           ResultSet result=DB.Select("*", "user_selected_option_values","User_option_id="+OptionID);
           while(result.next())
           {
               NumberOfEmployees++;
           }            
        result = DB.Select("*", "Date", "Date_ID=" + Date_ID);                
          while (result.next())
          {
               Date = result.getString("Date");
           }            
           while(Dates.size()<3)
           {
                  FDATE = "";
           int FID = Character.getNumericValue(Date.charAt(Date.length()-1)) + Character.getNumericValue(Date.length()-2);
               FID++;
               for (int i = 0; i < Date.length()-1; i++)
               {
                   FDATE += Date.charAt(i);
               }
               FDATE += FID+DayPlus;                

            result = DB.Select("*", "time_chooser", "times=" + FDATE);
           while (result.next())
           {
               FID = result.getInt("Time_Chooser_ID");
           }
           result = DB.Select("*", "time_choosed", "Time_chooser_id=" + FID +" and Branch_ID="+Branch_ID);
           while (result.next()) 
           {
               NumberOfOrdersAtDay++;
           }
           if(NumberOfEmployees*5>NumberOfOrdersAtDay)
           {
               Dates.add(FDATE);
           }
           DayPlus++;
           }
           
       } 
       catch (Exception E) 
       {
           System.out.println("");
       }
       Chose.setTime(Dates);
       DB.Close();
       return Chose;
   }
   
   
   
  //omar 0_0
  private int Employee_order_numper(ArrayList<Integer> orders,int Employee_id)
  {
      int num = 0;
      try {
            DB_controller Db = DB_controller.Get_DB_controller();
            Db.Connect();
            ResultSet res ;
            int id = 0 ;
            for(int x : orders)
            {
                res = Db.Select("Technical_id", "device_of_this_request", "Order_fexable_id="+x);
                while(res.next())
                {
                    id = res.getInt("Technical_id");
                    if(Employee_id == id)
                        num++;
                }
            }
     } catch (SQLException ex) {
        Logger.getLogger(Service_Management.class.getName()).log(Level.SEVERE, null, ex);
     }
     return num;
  }
   
   
   //omar
   public HashMap<Employee,Integer> Show_empty_technical(String Date,int Branch_id) {
        try {
            DB_controller Db = DB_controller.Get_DB_controller();
            Db.Connect();
            ArrayList<Integer> aa = new ArrayList<>();
            ResultSet res = Db.Select("Time_chooser_id", "time_choosed", "Branch_id="+Branch_id);
            int x = 0;
            int request_id = 0;
            while(res.next())
            {
                x = res.getInt("Time_chooser_id");
                ResultSet res2 =Db.Select("Request_id", "time_chooser", "Time_chooser_id="+x+" and Times="+Date);
                while (res2.next()) {                    
                    request_id = res2.getInt("Request_id");
                    ResultSet res3 = Db.Select("Order_fixable_id", "order_fixable", "Requist_id="+request_id);
                    while(res3.next())
                    {
                        aa.add(res3.getInt("Order_fixable_id"));
                    }
                }
            }
                System_manage sy = System_manage.Get_System_manage();
                Branch branch = sy.Search_branch(Branch_id);
                HashMap<Employee,Integer>  H = new HashMap<>();
                ArrayList<Employee> semp = branch.Show_employee();
                int xx = 0;
                for(Employee em : semp)
                {
                    xx = Employee_order_numper(aa,em.getID());
                    if(x<5)
                    {
                        H.put(em, x);
                    }
                }
            return H;
        } catch (SQLException ex) {
            Logger.getLogger(Service_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
    

   
    public ArrayList<Integer> Date_Choose() {
        return null;
    }

    
    boolean Accept_Request(int Request_id) {
        return true;
    }

    
    Bill_inf Add_Componenet(ArrayList<Component> Component, Bill bill, String Technical_description) {
        return null;
    }

    //Emad
    public ArrayList<Order> Show_My_Order(int Employee_id) {
        ArrayList<Order> or = new ArrayList<Order>();
        DB_controller DB = DB_controller.Get_DB_controller();
        ResultSet result = DB.Select("*", "order_fixable", "Service_ID=" + Employee_id);
        ResultSet result2 = null;
        int ID = -1;
        try {
            while (result.next()) {
                Order O = new Order();
                ArrayList<Integer> S = new ArrayList<Integer>();
                ID = result.getInt("Order_fixable_id");
                O.setState(result.getInt("State_id"));
                O.setDate_start_id(result.getInt("Date_start_id"));
                O.setDate_end_id(result.getInt("recept_Date_id"));
                O.setTecnical_description(result.getInt("Technical_description"));
                result2 = DB.Select("*", "device_of_this_request", "Device_of_this_request_id=" + ID);
                while (result2.next()) {
                    S.add(result.getInt("Technical_ID"));
                }
                O.setMy_Technical_id(S);
                or.add(O);
            }
        } catch (Exception E) {

        }
        DB.Close();
        return or;
    }

    //Emad
    //pre Path Type_OPTION_ID(text,int,....),and Name OF Type
    //post Add to Table user_option 
    public int add_option(int Type_ID, String Name) {
        DB_controller DB = DB_controller.Get_DB_controller();

        DB.Connect();
        HashMap<String, String> H = new HashMap<String, String>();
        H.put("Name", Name);
        H.put("Type_id", Integer.toString(Type_ID));
        return DB.Insert("user_option", H);
    }

    //Emad
    public ArrayList<Order> Show_all_order(int state) {
        DB_controller DB = DB_controller.Get_DB_controller();
        DB.Connect();
        ArrayList<Order> O = new ArrayList<Order>();
        ResultSet result = DB.Select("*", "order_fixable", "State_id=" + state);
        try {
            while (result.next()) {
                Order order = new Order();
                order.setId(result.getInt("Order_fixable_id"));
                order.setDate_start_id(result.getInt("Date_start_id"));
                order.setDate_end_id(result.getInt("recept_date_id"));
                order.setState(result.getInt("state_id"));
                order.setTecnical_description(result.getInt("Technical_description"));
                order.setMy_service_id(result.getInt("Service_id"));
                order.setMy_requist_id(result.getInt("Requist_id"));
                O.add(order);
            }
        } catch (Exception e) {
            System.out.println("Error in Show_all_ORDER");
        }
        DB.Close();
        return O;
    }

    boolean Cancel_Request(int Request_id) {
        return true;
    }

    //Mohamed RAdwan
    public Request Search_requist(int Requist_id) {
        try {
            //Table request
            DB_controller DB = DB_controller.Get_DB_controller();
            DB.Connect();
            Request request = new Request();
            ResultSet res = DB.Select("*", "request", "Request_id=" + Requist_id);
            request.setID(Requist_id);
            request.setState_id(res.getInt("ate_id"));
            request.setDate_id(res.getInt("Date_id"));//lw h8er el Date id to int 
            ArrayList<Integer> Devices_ID = new ArrayList<Integer>();
            //END Table Reqest

            //Table device_of_this_request
            ResultSet Devices = DB.Select("Device_id", "device_of_this_request", "request_id=" + Requist_id);
            while (Devices.next()) {
                Devices_ID.add(Devices.getInt("Device_id"));
            }
            request.setDevice_id(Devices_ID);
            return request;
        }//END TRy
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Mohamed RAdwan

    public boolean Give_order(Order order) {
        try {
            DB_controller DB = DB_controller.Get_DB_controller();
            DB.Connect();
            //Table order_fixable 
            HashMap<String, String> Order = new HashMap<>(10);
            Order.put("Requist_id", String.valueOf(order.getMy_requist_id()));
            Order.put("Date_start_id", String.valueOf(order.getDate_start_id()));
            Order.put("Technical_description", Integer.toString(order.getTecnical_description()));
            Order.put("Service_id", String.valueOf(order.getMy_service_id()));
            Order.put("State_id", "3");//wait fix
            Order.put("date_End_id", String.valueOf(order.getMy_requist_id()));
            DB.Insert("order_fixable", Order);
            //end teabl order_fixable

            //Table device_of_this_request
            HashMap<String, String> ordflexde = new HashMap<>(1);
            int counter = 0;
            Request req = Search_requist(order.getMy_requist_id());
            ArrayList<Integer> dev = req.getDevice_id();
            int id_request = req.getID();
            ArrayList<Integer> ID_rder_flexible_details = new ArrayList<>();
            for (Integer d : dev) {
                ResultSet result = DB.Select("Device_of_this_request_id", "device_of_this_request", "Device_id=" + d + " request_id=" + id_request);
                ID_rder_flexible_details.add(result.getInt("Device_of_this_request_id"));
            }

            //if you want to use it in table Device_of_this_request_id to insert in
            /*for(Integer d:dev)
            {
                ordflexde.put("Flexible_id",String.valueOf(id_order));
                ordflexde.put("Device_id",String.valueOf(d));
                DB_controller.Insert("order_flexible_details",ordflexde);
                ID_rder_flexible_details.add(d);
                ordflexde=new HashMap<>(1);
            }*/
            //END device_of_this_request
            //Table order_flixer
            ArrayList<Integer> Techincal_ID = order.getMy_Technical_id();
            counter = 0;
            for (Integer d : ID_rder_flexible_details) {
                ordflexde.put("Technical_id", String.valueOf(Techincal_ID.get(counter)));
                ordflexde.put("Order_id", String.valueOf(d));
                DB_controller.Get_DB_controller().Insert("order_flixer", ordflexde);
                ordflexde = new HashMap<>(1);
                counter++;
            }//end table order_flixer

        }//end try
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
//Emad

    public Device Search_device(int Device_id) {
        DB_controller DB = DB_controller.Get_DB_controller();
        ResultSet result = DB.Select("*", "Device", "Device_ID=" + Device_id);
        int Model = -1;
        int companyID = -1;
        int Type_id = -1;
        Device D = new Device();
        try {
            while (result.next()) {
                D.setId(result.getInt("Device_id"));
                D.setState(result.getInt("state_id"));
                Model = result.getInt("Model_id");
            }
            result = DB.Select("*", "model", "Model_id=" + Model);
            while (result.next()) {
                D.push("Model", result.getString("Name"));
                companyID = result.getInt("Comp_device_id");
            }
            result = DB.Select("*", "main_factor", "Main_factor_id=" + companyID);
            while (result.next()) {
                D.push("Copmany", result.getString("Name"));
            }
            result = DB.Select("*", "company_have_device", "Main_factor_id=" + companyID);
            while (result.next()) {
                Type_id = result.getInt("Device_type_id");
            }
            result = DB.Select("*", "Device_type", "Device_Type_id=" + Type_id);
            while (result.next()) {
                D.push("Type", result.getString("name"));
            }
            return D;
        } catch (Exception E) {
            System.out.println("Error in Search_DEVICE");
        }
        return null;
    }

   
    //omar 0_0
    public String get_address_from_db(int Address_id) {
        DB_controller DB = DB_controller.Get_DB_controller();
        DB.Connect();
        try {
            ResultSet res = DB.Select("*", "address", "Address_id=" + Address_id);
            if (res == null) {
                return "";
            }
            while (res.next()) {
                if (res.getInt("Parent_id") == 0) {
                    String s = res.getString("Address");
                    return s;
                } else {
                    int x = res.getInt("Parent_id");
                    String s = res.getString("Address");
                    return s + "," + get_address_from_db(x);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(System_manage.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

//omar 0_0
    public boolean Add_new_address_to_user(int District_id, String Street, String Num_of_home, int user_id) {
        if (Validations.Get_Validations().Is_alphanumaric(Street) && Validations.Get_Validations().Is_alphanumaric(Num_of_home)) {
            try {
                DB_controller.Get_DB_controller().Connect();
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("Address", Street);
                hash.put("Parent_id", Integer.toString(District_id));
                int x = DB_controller.Get_DB_controller().Insert("address", hash);
                hash.clear();
                hash.put("Address", Num_of_home);
                hash.put("Parent_id", Integer.toString(x));
                x = DB_controller.Get_DB_controller().Insert("address", hash);
                hash.clear();
                hash.put("Address_id", Integer.toString(x));
                hash.put("User_id", Integer.toString(user_id));
                DB_controller.Get_DB_controller().Insert("user_address", hash);
                DB_controller.Get_DB_controller().Close();
                return true;

            } catch (Exception ex) {
                Logger.getLogger(User.class
                        .getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
//omar 0_0

    public boolean Delete_user_address(int Address_id, int user_id) {
        DB_controller.Get_DB_controller().Connect();
        boolean z = DB_controller.Get_DB_controller().Delete("address", "Address_id=" + Address_id);
        DB_controller.Get_DB_controller().Delete("user_address", "Address_id=" + Address_id + " and User_id=" + user_id);
        DB_controller.Get_DB_controller().Close();
        return z;
    }

    //omar  0_0
    public boolean Update_address(int Old_num_of_home_id, String new_Street, String new_num_of_home) {
        if (Validations.Get_Validations().Is_alpha(new_Street) && Validations.Get_Validations().Is_alphanumaric(new_num_of_home)) {
            try {
                DB_controller.Get_DB_controller().Connect();
                Data_access.DB_controller.Get_DB_controller().Update("address", "Address=" + new_num_of_home, "Address_id=" + Old_num_of_home_id);
                ResultSet res = DB_controller.Get_DB_controller().Select("Parent_id", "address", "Address_id=" + Old_num_of_home_id);
                int x = 0;
                while (res.next()) {
                    x = res.getInt("Parent_id");
                }
                boolean z = DB_controller.Get_DB_controller().Update("address", "Address='" + new_Street + "'", "Address_id=" + x);
                Data_access.DB_controller.Get_DB_controller().Close();
                return z;

            } catch (SQLException ex) {
                Logger.getLogger(System_manage.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    //omar  0_0
    public HashMap<Integer, String> Get_User_Address(int User_id) {
        DB_controller Db = DB_controller.Get_DB_controller();
        try {
            HashMap<Integer, String> m = new HashMap<>();
            Db.Connect();
            ResultSet res = Db.Select("Address_id", "user_address", "User_id=" + User_id);
            int x = 0;
            while (res.next()) {
                x = res.getInt("Address_id");
                m.put(x, this.get_address_from_db(x));
            }
            DB_controller.Get_DB_controller().Close();
            return m;

        } catch (SQLException ex) {
            Logger.getLogger(User.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        Db.Close();
        return null;
    }

    //omar 0_0
    public int Add_New_Phone_To_User(String New_phone, int User_id) {
        Validations v = Validations.Get_Validations();
        if (!v.Is_digit(New_phone)) {
            return 0;
        }
        DB_controller.Get_DB_controller().Connect();
        HashMap<String, String> m = new HashMap<>();
        m.put("User_id", Integer.toString(User_id));
        m.put("Phone", New_phone);
        int x = DB_controller.Get_DB_controller().Insert("phone", m);
        DB_controller.Get_DB_controller().Close();
        return x;
    }

    //omar 0_0
    public boolean Delete_User_Phone(int Phone_id) {
        DB_controller.Get_DB_controller().Connect();
        boolean z = DB_controller.Get_DB_controller().Delete("phone", "phone_id=" + Phone_id);
        DB_controller.Get_DB_controller().Close();
        return z;
    }

    //omar 0_0
    public boolean Update_User_Phone(int Old_phone_id, String New_phone) {
        if (!Validations.Get_Validations().Is_digit(New_phone)) {
            return false;
        }
        DB_controller.Get_DB_controller().Connect();
        boolean z = DB_controller.Get_DB_controller().Update("phone", "phone=" + New_phone, "Phone_id=" + Old_phone_id);
        DB_controller.Get_DB_controller().Close();
        return z;
    }

    //omar 0_0
    public HashMap<Integer, String> Get_User_Phone(int User_id) {
        try {
            HashMap<Integer, String> m = new HashMap<>();
            DB_controller.Get_DB_controller().Connect();
            ResultSet res = DB_controller.Get_DB_controller().Select("*", "phone", "User_id=" + User_id);
            while (res.next()) {
                m.put(res.getInt("Phone_id"), res.getString("phone"));
            }
           
            return m;

        } catch (SQLException ex) {
            
        }
        
        return null;
    }

///sala7
    public String Show_satate(int state) {
        DB_controller.Get_DB_controller().Connect();
        ResultSet result = DB_controller.Get_DB_controller().Select("State", " state ", " State_id = " + state);
        String res = "";
        try {
            while (result.next()) {
                res = result.getString("State");
            }
            DB_controller.Get_DB_controller().Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }
    //Emad

    public ArrayList<Complain> Show_complains(int State) {
        DB_controller.Get_DB_controller().Connect();
        ResultSet result = DB_controller.Get_DB_controller().Select("*", "Message_type", "Name='Complain'");
        ArrayList<Complain> C = new ArrayList<Complain>();
        int ID = 0;
        try {
            while (result.next()) {
                ID = result.getInt("Message_type_id");
            }
            result = DB_controller.Get_DB_controller().Select("*", "recieved", "Message_id=" + ID + " and State_id=" + State);
            while (result.next()) {

                Complain complain = new Complain();
                complain.setId(ID);
                complain.setReciver(result.getInt("Reciever_id"));
                complain.setState(State);
                C.add(complain);
            }
            return C;
        } catch (Exception E) {
            System.out.println("Error in Complains");
        }
        return null;
    }

}
