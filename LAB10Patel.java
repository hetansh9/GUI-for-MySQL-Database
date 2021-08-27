
import java.sql.*;

import javax.swing.*;

public class LAB10Patel {
    


    public static void main (String[] args){

        Connection con = null;
        try {
        Statement stmt;

        // Register the JDBC driver for MySQL.
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Define URL of database server for
        // database named 'user' on the faure.
        String url =
                "jdbc:mysql://faure/hetansh";

        // Get a connection to the database for a
        // user named 'user' with the password
        // 123456789.
        con = DriverManager.getConnection(
                            url,"hetansh", "831616927");

        // Display URL and connection information

        System.out.println("URL: " + url);
        System.out.println("Connection: " + con);
        // Creating statements 
        stmt = con.createStatement();

        while(!con.isClosed()){
            System.out.println("Please provide the MemberID");
            String memberID = JOptionPane.showInputDialog ("Please provide the MemberID");
            //System.out.println("Correct value ?" + memberID);
            if (memberID == "" || memberID.equals("null")){
                System.exit(0);
            }
            String query = "Select Count(*) from Member WHERE MemberID = "+ memberID+";";
            ResultSet rs = stmt.executeQuery(query); 
                while(rs.next()){
                    int count = rs.getInt(1);
                    if(count>0){
                        JOptionPane.showMessageDialog(null, "Member " + memberID + " is in our database");
                        String[] options = {"Yes", "No"};
                        int bookcheck = JOptionPane.showOptionDialog(null, "Would you like to checkout a book from our Library?", "Please Select your answer", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "default");
                        if(bookcheck == JOptionPane.NO_OPTION){
                            System.exit(0);
                        }else {
                            Object[] searchOptions = {"By ISBN", "By BookName", "By Author", "Cancel Search"};
                            int search = JOptionPane.showOptionDialog(null, "Please Select your Option", "Methods through which you can search for your book", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, searchOptions, searchOptions[0]);
// ---------------------------- Search by ISBN ------------------------------------------------------------------------
                            if (search == 0){
                                String ISBN  = JOptionPane.showInputDialog("Enter the Book ISBN in XX-XXXXX-XXXXX format");

                                String isbnQuery = "SELECT *from Book where ISBN = '" + ISBN+"';";
                                System.out.println("isbn query " + isbnQuery);
                                PreparedStatement ps = con.prepareStatement(isbnQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet ri = ps.executeQuery();
                                ri.last();
                                int checkforBooks = ri.getRow();
                                ri.beforeFirst();
                                //JOptionPane.showMessageDialog(null, "Total book we have with the following ISBN = " + checkforBooks);
                                String sqlquery1 = "Select lname, copies_not_checkedout, shelf, at_floor from locatedAt WHERE copies_not_checkedout >0 AND ISBN = '" + ISBN + "';";
                                PreparedStatement ps1 = con.prepareStatement(sqlquery1,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet rows_return = ps1.executeQuery();
                                rows_return.last();
                                int ava = rows_return.getRow();
                                rows_return.beforeFirst();
                                System.out.println("Copies available" + ava);
                                if(checkforBooks >0 && ava>0){
                                    while(rows_return.next()){
                                        String lname = rows_return.getString("lname");
                                        int copies = rows_return.getInt("copies_not_checkedout");
                                        int shelf = rows_return.getInt("shelf");
                                        int floor = rows_return.getInt("at_floor");
                                        //System.out.println("title is " + title);
                                        JOptionPane.showMessageDialog(null, "The book with ISBN = " +ISBN + " is currently located at the " + lname + " Library on the  " + shelf + " shelf of the  " + floor + " floor and has " + copies + " total copies available");
                                    }
                                }
                                else if (checkforBooks >0 && ava<=0){
                                    JOptionPane.showMessageDialog(null, "The book you are looking for is currently checkedout", "Book Information with ISBN search", JOptionPane.INFORMATION_MESSAGE);
                                }
                                break;
                            }
                            // ---------------------------- Search by Book ------------------------------------------------------------------------
                            else if(search == 1){
                                String bname = JOptionPane.showInputDialog(null, "Please enter the name of the book ");
                                ResultSet bookname;
                                String bquery = "Select*from Book where Title = '" + bname+"';";
                                PreparedStatement ps2 = con.prepareStatement(bquery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                bookname = ps2.executeQuery(bquery);
                                int i =0;
                                bookname.last();
                                Object[][] match = new String[bookname.getRow()][];
                                bookname.beforeFirst();
                                String resulting_query = "Do you want the following book ?\n\n                      ISBN                                    Title                             YearPublished\n";
                                while(bookname.next()) {
                                        String ISBN = bookname.getString("ISBN");
                                        String Title = bookname.getString("Title");
                                        String year = bookname.getString("year_published");
                                        match[i] = new String[] {ISBN, Title, year};
                                        i++;
                                        String s = i + ". " + ISBN + "     " + Title + "      " + year + "    " + "\n";
                                        resulting_query += s;
                                }
                                resulting_query += "Enter the number of the book: \n";
                                String option = JOptionPane.showInputDialog(null, resulting_query);
                                int c = Integer.parseInt(option);
                                String book_isbn = match[c-1][0].toString();

                                String isbnQuery = "SELECT *from Book where ISBN = '" + book_isbn+"';";
                                System.out.println("isbn query " + isbnQuery);
                                PreparedStatement ps = con.prepareStatement(isbnQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet ri = ps.executeQuery();
                                ri.last();
                                int checkforBooks = ri.getRow();
                                ri.beforeFirst();
                                //JOptionPane.showMessageDialog(null, "We have "+ checkforBooks+" copies of thE book with provided ISBN");
                                String sqlquery1 = "Select lname, copies_not_checkedout, shelf, at_floor from locatedAt WHERE copies_not_checkedout >0 AND ISBN = '" + book_isbn + "';";
                                PreparedStatement ps1 = con.prepareStatement(sqlquery1,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet rows_return = ps1.executeQuery();
                                rows_return.last();
                                int ava = rows_return.getRow();
                                rows_return.beforeFirst();
                                System.out.println("Copies available" + ava);

                                if(checkforBooks >0 && ava>0){
                                    while(rows_return.next()){
                                        String lname = rows_return.getString("lname");
                                        int copies = rows_return.getInt("copies_not_checkedout");
                                        int shelf = rows_return.getInt("shelf");
                                        int floor = rows_return.getInt("at_floor");
                                        JOptionPane.showMessageDialog(null, "The book with ISBN = " + book_isbn + " is currently located at the " + lname + " Library on the  " + shelf + " shelf of the  " + floor + " floor and has " + copies + " total copies available");


                                    }
                                }
                                else if (checkforBooks >0 && ava<=0) {
                                    JOptionPane.showMessageDialog(null, "The book you are looking for is currently checkedout", "Book Information with ISBN search", JOptionPane.INFORMATION_MESSAGE);

                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "The Selected book with the ISBN = "+book_isbn+" is out of stock");

                                }

                            }
// ------------------------------------------------- Search by Author Name ---------------------------------------------------------------------                            
                            else if(search == 2){
                                String author = JOptionPane.showInputDialog("Please provide the Author Name. Ex - 'firstname lastname'");
                                String author_split[] = author.split(" ");
                                
                                String author_firstname = author_split[0];
                                String author_lastname = author_split[1];
                                String author_query = "Select b.ISBN, b.Title from Book b, WrittenBy w , Author a where b.ISBN = w.ISBN AND a.AuthorID = w.AuthorID AND a.first_name = '" + author_firstname +"' AND a.last_name = '" + author_lastname + "';";
                                PreparedStatement ps3 = con.prepareStatement(author_query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet author_rs = ps3.executeQuery();

                                int i=0;
                                author_rs.last();
                                Object[][] match = new String[author_rs.getRow()][];
                                author_rs.beforeFirst();
                                String author_result = "The following books are associated for the Author you selected\n        Title                                                      ISBN\n";
                                while(author_rs.next()){
                                    String isbn = author_rs.getString("ISBN");
                                    String title = author_rs.getString("Title");
                                    match[i] = new String[] {isbn, title};
                                    i++;
                                    String s = i+". " + title + "        " + isbn + "      " +"\n";
                                    author_result += s;

                                }
                                author_result += "Enter the number below. Example - '1,2,3'";
                                String toParse = JOptionPane.showInputDialog(author_result);
                                int check1 = Integer.parseInt(toParse);
                                String author_isbn = match[check1-1][0].toString();


                                String isbnQuery = "SELECT *from Book where ISBN = '" + author_isbn+"';";
                                System.out.println("isbn query " + isbnQuery);
                                PreparedStatement ps = con.prepareStatement(isbnQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet ri = ps.executeQuery();
                                ri.last();
                                int checkforBooks = ri.getRow();
                                ri.beforeFirst();
                                //JOptionPane.showMessageDialog(null, "Total book we have with the following ISBN = " + checkforBooks);
                                String sqlquery1 = "Select lname, copies_not_checkedout, shelf, at_floor from locatedAt WHERE copies_not_checkedout >0 AND ISBN = '" + author_isbn + "';";
                                PreparedStatement ps1 = con.prepareStatement(sqlquery1,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                ResultSet rows_return = ps1.executeQuery();
                                rows_return.last();
                                int ava = rows_return.getRow();
                                rows_return.beforeFirst();
                                System.out.println("Copies available" + ava);

                                if(checkforBooks >0 && ava>0){
                                    while(rows_return.next()){
                                        String lname = rows_return.getString("lname");
                                        int copies = rows_return.getInt("copies_not_checkedout");
                                        int shelf = rows_return.getInt("shelf");
                                        int floor = rows_return.getInt("at_floor");
                                        JOptionPane.showMessageDialog(null, "The book with ISBN = " +author_isbn + " is currently located at the " + lname + " Library on the  " + shelf + " shelf of the  " + floor + " floor and has " + copies + " total copies available");
                                    }
                                }
                                else if (checkforBooks >0 && ava<=0){
                                    JOptionPane.showMessageDialog(null, "The book you are looking for is currently checkedout", "Book Information with ISBN search", JOptionPane.INFORMATION_MESSAGE);
                                }
                                
                                else {
                                    JOptionPane.showMessageDialog(null, "The Selected book with the ISBN = "+author_isbn+" is out of stock");
                                }



                            }
                        }


                    }else {

                        JOptionPane.showMessageDialog(null, "Invalid Member ID");
                        String[] options = {"Yes", "No"};
                        int tocheckForNoOption = JOptionPane.showOptionDialog(null, "Would you like to add a new Member?", "Member not found, please specify your answer", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "default");
                        if(tocheckForNoOption == JOptionPane.NO_OPTION){
                            System.exit(0);
                        }
                        JTextField jd_memberID = new JTextField();
                        JTextField first_name = new JTextField();
                        JTextField last_name= new JTextField();
                        JTextField DOB = new JTextField();
                        JTextField gender = new JTextField();

                        Object[] details = {
                            "Enter MemberID", jd_memberID,
                            "Enter the First Name", first_name,
                            "Enter the Last Name", last_name,
                            "Enter the Date of Birth in YYYY-MM-DD Format", DOB,
                            "Enter Member Gender in M or F", gender
                        };
                        int memberoption = JOptionPane.showConfirmDialog(null, details, "Add details for the new Member", JOptionPane.OK_CANCEL_OPTION);
                        if(memberoption == JOptionPane.OK_OPTION){
                            String getJd_memberid = jd_memberID.getText();
                            String getJd_first_name = first_name.getText();
                            String getJd_last_name = last_name.getText();
                            String getJd_DOB = DOB.getText();
                            System.out.println("DOB IS " + getJd_DOB);
                            String getJd_gender = gender.getText();
                            String sqlquery = "INSERT INTO Member VALUES (" + getJd_memberid + ",'" + getJd_first_name+ "','" +getJd_last_name+"','"+getJd_DOB+"','" + getJd_gender+"');";
                            stmt.executeUpdate(sqlquery);
                            JOptionPane.showMessageDialog(null, "New Member has been added");

                            break;
                        }
                        else if (memberoption == JOptionPane.CANCEL_OPTION){
                            System.exit(0);
                        }
                        else {
                            break;
                        }
                    }
                }

        }

        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

}
