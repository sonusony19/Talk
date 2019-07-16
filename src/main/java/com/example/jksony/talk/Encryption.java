package com.example.jksony.talk;

public class Encryption {
    public Encryption() {
    }

    public String Encrypted(String s)
    {
        int size = s.length();
        if(size==0) return "";
         char [] sarray = s.toCharArray();
         char [] encryptedarray = s.toCharArray();
         for(int i=0;i<size;i++)
         {
            int a = sarray[i];
          //  int b = s.charAt(i);
            int c = 97-a;
            encryptedarray[i] = Character.valueOf((char)c);
         }

         return  String.copyValueOf(encryptedarray);
    }

    public String deEncrypted(String s)
    {
        int size = s.length();
        if(size==0) return "";
        char [] sarray = s.toCharArray();
        char [] encryptedarray = s.toCharArray();
        for(int i=0;i<size;i++)
        {
            int a = sarray[i];
            //  int b = s.charAt(i);
            int c = 97-a;
            encryptedarray[i] = Character.valueOf((char)c);
        }

        return  String.copyValueOf(encryptedarray);
    }

}
