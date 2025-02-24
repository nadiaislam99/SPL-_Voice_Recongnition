package com.TeamEnigma.Gui;
import javax.swing.*;
import java.awt.*;

public class Button_templete extends JFrame {
    public JButton  speak, match, login,submit;
    public JTextField UserName;
    public Container container ;
    public JLabel display,usernamelabel,passwordlabel;
    private Font font;
    public JPasswordField Password;
    public JDialog jDialog;

    public void frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(250, 180, 435, 355);
        this.setResizable(false);
    }

    public void container(){
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(128, 219, 219));
        Button_Method();
    }

    public void Button_Method(){
        jDialog = new JDialog();
        jDialog.setTitle("DialogBox");
        JLabel l = new JLabel("this is a dialog box");

        jDialog.add(l);
        jDialog.setSize(100, 100);
        jDialog.add(l);
        container.add(l);
        //container.add(jDialog);
      //  jDialog.setContentPane(container);




        /*display = new JTextField();
        display.setBounds(40, 28, 341, 45);
        display.setEditable(false);
        container.add(display);*/
        font = new Font("Arial",Font.BOLD ,18 );
        display = new JLabel();
        display.setBounds(40, 28, 500, 60);
        display.setText("I am ok ,But this is not Good for health. I need more.You can join me.");
        container.add(display);
        display.setFont(font);

        speak = new JButton("Speak");
        speak.setBounds(130, 150, 93, 50);
        container.add(speak);

        match = new JButton("Match");
        match.setBounds(260, 150, 93, 50);
        container.add(match);

        login = new JButton("Log In");
        login.setBounds(390, 150, 93, 50);
        container.add(login);

        UserName = new JTextField();
        UserName.setBounds(190, 350, 300, 45);
        UserName.setVisible(false);
        container.add(UserName);

        Password = new JPasswordField();
        Password.setBounds(190, 415, 300, 45);
        Password.setText( "");
        Password.setVisible(false);
        container.add(Password);

        submit = new JButton("Submit");
        submit.setBounds(250, 480, 99, 51);
       // submit.setText( "");
        submit.setVisible(false);
        container.add(submit);

        usernamelabel = new JLabel("User ID : ");
        usernamelabel.setBounds(50,350,80,45);
        usernamelabel.setVisible(false);
        usernamelabel.setFont(font);
        container.add(usernamelabel);


        passwordlabel = new JLabel("Password : ");
        passwordlabel.setBounds(50,415,110,45);
        passwordlabel.setVisible(false);
        passwordlabel.setFont(font);
        container.add(passwordlabel);




    }


}
