package SE;

import Data_access.DB_controller;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moroclash
 */
public class User {
    
    private int ID;
    private String F_name;
    private String L_name;
    private String Email;
    private String Password;
    private int Type_id;
    private String Gander;
    private HashMap<Integer, String> Additional_data;
    private HashMap<Integer,String> Phones;
    private HashMap<Integer,String> Addresses;
    private ArrayList <Massage> Inbox;
    private boolean Block;

    public void setAddresses(HashMap<Integer, String> Addresses) {
        this.Addresses = Addresses;
    }

    public ArrayList<Massage> getInbox() {
        return Inbox;
    }

    
    public void push(int Key,String Value)
   {
    this.Phones.put(Key, Value);
   }
   
    public void setBlock(boolean Block) {
        this.Block = Block;
    }
    
    public boolean getBlock()
    {
        return this.Block;
    }
 
    public void setID(int ID) {
        this.ID = ID;
    }
    public HashMap<Integer, String> getAdditional_data() {
        return Additional_data;
    }

    public void setAdditional_data(HashMap<Integer, String> Additional_data) {
        this.Additional_data = Additional_data;
    }

    public int getID() {
        return ID;
    }
//omar
    public boolean setEmail(String Email) {
        if(!Validations.Is_email(Email))
            return false;
        this.Email = Email;
        return true;
    }
 public void setPhones(HashMap<Integer, String> Phones) {
        this.Phones = Phones;
    }

    public HashMap<Integer, String> getPhones() {
        return Phones;
    }
    public String getEmail() {
        return Email;
    }
    //omar
    public boolean setGander(String Gander) {
        if(!Validations.Is_gender(Gander))
            return false;
        this.Gander = Gander;
        return true;
    }

    public String getGander() {
        return Gander;
    }
    //omar
    public boolean setL_name(String L_name) {
        if(!Validations.Is_alpha(L_name))
            return false;
        this.L_name = L_name;
        return true;
    }

    public String getL_name() {
        return L_name;
    }
    
    //omar
    public boolean setPassword(String Password) {
        if(!Validations.Is_passord(Password))
            return false;
        this.Password = Password;
        return true;
    }

    public String getPassword() {
        return Password;
    }
    
    //omar
    public void setType_id(int Type_id) {
        this.Type_id = Type_id;
    }

    public int getType_id() {
        return Type_id;
    }


    public HashMap<Integer, String> getAddresses() {
        return Addresses;
    }    
    //omar
    public boolean setF_name(String F_name) {
        if(!Validations.Is_alpha(F_name))
            return false;
        this.F_name = F_name;
        return true;
    }

    public String getF_name() {
        return F_name;
    }

    //Sala7
    public boolean Update_user() {
        System_manage m = new System_manage();
        DB_controller.Connect();
        
            Employee emp = new Employee();
            DB_controller.Update(" user ", " Fname =" + emp.getF_name() + " Lname = " + emp.getL_name() + " Password = " + emp.getPassword()
                    + " Email = " + emp.getEmail() + " gender = " + emp.getGander(), " User_id = " + ID);
            DB_controller.Update("  ", "  ", "  ");
        
        DB_controller.Close();
        return true;
    }
    
    
    //omar
    public ArrayList<General_massge> Show_all_my_rescived_massage() {
        try {
            ArrayList<General_massge> m = new ArrayList<>();
            Data_access.DB_controller.Connect();
            ResultSet res = Data_access.DB_controller.Select("Message_id", "recieved", "Reciever_id="+getID());
            int x=0;
            while(res.next())
            {
                x = res.getInt("Message_id");
                General_massge ms =(General_massge) new System_manage().Search_Massage(x);
                m.add(ms);
            }
            Data_access.DB_controller.Close();
            return m;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
  
   
    
   
    
    
    //omar
    public void Add_massage(General_massge New_massage) {
        new System_manage().Send_Message(New_massage);
    }
    
    

    
    
   

    public void Add_new_phone(String New_phone) {

    }

    public boolean Delete_phone(int phone_id) {
        return false;
    }

    public boolean Update_phone(int Old_phone_id, String New_phone) {
        return false;
    }

    


    
 
    
    

    //Emad
    //pre Path the name of new user and the parent id 
    //post add to table type user
    public int add_new_user(String Name,int Parent_id)
    {
        DB_controller.Connect();
        HashMap<String,String> H=new HashMap<String,String>();
        H.put("Name", Name);
        H.put("parent_id",Integer.toString(Parent_id));
        return DB_controller.Insert("type_user", H);
    }
    
    //Emad
    public String Search_OptionByID(int Option_ID)
    {
        DB_controller.Connect();
        ResultSet result=DB_controller.Select("*","user_option","User_Option_ID="+Option_ID);
        try
        {
            while(result.next())
            {
                return result.getString("Name");
            }
        }
        catch(Exception E)
        {
            System.out.println("Error in Search Option");
        }
        return null;
    }
    //Emad
    public int Search_OptionByName(String Option_Name)
    {
        DB_controller.Connect();
        ResultSet result=DB_controller.Select("*","user_option","Name='"+Option_Name+"'");
        try
        {
            while(result.next())
            {
                return result.getInt("User_option_id");
            }
        }
        catch(Exception E)
        {
            System.out.println("Error in Search Option");
        }
        return -1;
    }
    //Emad
    //pre Path Type_OPTION_ID(text,int,....),and Name OF Type
    //post Add to Table user_option 
    public int add_option (int Type_ID,String Name)
    {
        DB_controller.Connect();
        HashMap<String,String> H=new HashMap<String,String>();
        H.put("Name",Name);
        H.put("Type_id", Integer.toString(Type_ID));
        return DB_controller.Insert("user_option", H);
    }
    //Emad
    public int Add_User_Option (int user_type_id,int Option_ID)
    {
        DB_controller.Connect();
            HashMap<String,String> H=new HashMap<String,String>();
            H.put("User_Type_ID",Integer.toString(user_type_id));
            H.put("user_option_id",Integer.toString(Option_ID));
            int i=DB_controller.Insert("User_Selected_Option", H);
        DB_controller.Close();
        return i;
    }
    //Emad
    public ArrayList<Integer> All_Options_Available(int Type_ID)
    {
        DB_controller.Connect();
        ArrayList<Integer> A=new ArrayList<Integer>();
        ResultSet result=DB_controller.Select("*", "user_selected_option","user_type_id="+Type_ID);
        try
        {
            while(result.next())
            {
                A.add(result.getInt("user_option_id"));
            }
        }
        catch(Exception E)
        {
            System.out.println("Error in All Option Available");
        }
        DB_controller.Close();
        return A;
    }
    //Emad
    public void Insert_Option_Values(User U,int User_ID)
    {
        DB_controller.Connect();
        for(Map.Entry<Integer,String> entry:U.getAdditional_data().entrySet())
        {
            HashMap<String,String> H=new HashMap<String,String> ();
            H.put("User_ID",Integer.toString(User_ID));
            H.put("User_option_id",Integer.toString(entry.getKey()));
            H.put("value",entry.getValue());
            DB_controller.Insert("user_selected_option_values",H);
        }
        DB_controller.Close();
    } 
    //Emad
    public HashMap <Integer,String> Get_Option_Values_OF_USER(int  User_id)
    {
        HashMap <Integer,String> H=new HashMap<Integer,String> ();
        DB_controller.Connect();
        ResultSet result=DB_controller.Select("*","user_selected_option_values","User_ID="+User_id);
        try
        {
            while(result.next())
            {
                H.put((result.getInt("User_option_id")),result.getString("Value"));
            }
        }
        catch(Exception E)
        {
            System.out.println("Error in GET OPTION VALUES");
        }
        return H;
    }
    public HashMap <Integer,String> update_Option_Values_OF_USER(int  User_id)
    {
        HashMap <Integer,String> H=new HashMap<Integer,String> ();
        DB_controller.Connect();
        DB_controller.Update(" user_selected_option_values ", " * ", " User_id = " + User_id);
        return H;
    }
    //Emad
    public ArrayList<String> Get_Options_OF_TYPE(int  Type_ID)
    {
        ArrayList<String> A=new ArrayList<String>();
        DB_controller.Connect();
        ResultSet result=DB_controller.Select("*","user_option","Type_ID="+Type_ID);
        try
        {
            while(result.next())
            {
                A.add(result.getString("NAME"));
            }
        }
        catch(Exception E)
        {
            System.out.println("Error in GET OPTION OF TYPES");
        }
        return A;
    }    
    //////////////////////////////////////////////////////
    
   
    
    public ArrayList <Massage> Load_inbox()
    {
        return null;
    }
    
    public boolean Log_in()
    {
        return true;
    }
    
     //sala7
    public Object Log_in(String User_Name, String Password) {
        
        if(Validations.Is_email(User_Name) && Validations.Is_passord(Password))
        {
            DB_controller.Connect();
            System_manage system = new System_manage();
            Employee employee ;
            Customer customer ;
            
            ResultSet result = DB_controller.Select(" name ", " type_user ", " Type_user_id = 5 ");
            String name = "";
            try {
                while(result.next())
                {
                    name = result.getString("name");
                }
            } catch (SQLException ex) {
            }
            result =DB_controller.Select("User_id", " user ", " Email =  '" + User_Name + "'" + " and " + "Password = '" + Password +"'");
            try {
                while(result.next())
                {
                    if(name.equals("customer"))
                    {
                        customer = (Customer) system.Search_user_by_id(result.getInt("User_id"));
                        return customer;
                    }
                    else{
                        employee = (Employee) system.Search_user_by_id(result.getInt("User_id"));
                        return employee;
                    }
                }
            } catch (SQLException ex) {
            }
        }
        else{
            System.err.println("username and password is incorrect");
        }
        return null;
    }
    
    //omar
    public boolean DeleteMassge_that_send(int Massage_id) {
            boolean z = false;
        try {
            Data_access.DB_controller.Connect();
            ResultSet res2 = Data_access.DB_controller.Select("Reciever_id","recieved", "Message_id="+Massage_id);
            int x = 0;
            while (res2.next()) {
               x = res2.getInt("Reciever_id");
            }
            z = new System_manage().Update_massage_state(Massage_id, x, 6);
            Data_access.DB_controller.Close();
            } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        return z;
    }
   
     //omar
    public ArrayList<General_massge> Show_my_massage_send() {
        try {
            ArrayList<General_massge> m = new ArrayList<>();
            Data_access.DB_controller.Connect();
            ResultSet res = Data_access.DB_controller.Select("Message_id", "message", "sender_id="+getID());
            int x=0;
            while(res.next())
            {
                x = res.getInt("Message_id");
                General_massge ms =(General_massge) new System_manage().Search_Massage(x);
                m.add(ms);
            }
            Data_access.DB_controller.Close();
            return m;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     
    //omar
    public boolean Delete_Massge_that_resived(int Massage_id) {
            Data_access.DB_controller.Connect();
            boolean z = new System_manage().Update_massage_state(Massage_id, getID(), 7);
            Data_access.DB_controller.Close();
            return z;
    }
     //omar
    public ArrayList<General_massge> Show_my_massage(int State_id) {
        try {
            ArrayList<General_massge> m = new ArrayList<>();
            ResultSet res = Data_access.DB_controller.Select("Message_id", "recieved", "Reciever_id="+getID()+",State_id="+State_id);
            int x=0;
            while(res.next())
            {
                x = res.getInt("Message_id");
                General_massge ms =(General_massge) new System_manage().Search_Massage(x);
                m.add(ms);
            }
            return m;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}