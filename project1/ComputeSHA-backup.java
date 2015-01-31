import java.io.*;
import java.security.MessageDigest;

public class ComputeSHA 
{
   public static void main( String[] args )
   {
      File file = new File( args[0] );
      BufferedReader inputStream = null;
      System.out.println( args[0] );
      try {
         MessageDigest digest = MessageDigest.getInstance("SHA1");
         inputStream  = new BufferedReader( new FileReader(file) );
         int n = 0;
         //byte[] buffer = new byte[1024];
         String buffer;
         while ( (buffer=inputStream.readLine()) != null ) {
               System.out.println( buffer );
               digest.update(buffer.getBytes(), 0, buffer.length());
         }
         byte[] hashBytes = digest.digest();
            
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
         }
                                                      
         String fileHash = sb.toString();

         System.out.println( fileHash );

       } catch ( Exception e ) {
   
       }
       

   }


   public static String bytesToHex(byte[] in) {
      final StringBuilder builder = new StringBuilder();
      for(byte b : in) {
         builder.append(String.format("%02x", b));
      }
      return builder.toString();
   }
   /*public static byte[] createSha1(File file) {
       MessageDigest digest = MessageDigest.getInstance("SHA-1");
       InputStream fis = new FileInputStream(file);
       int n = 0;
       byte[] buffer = new byte[8192];
       while (n != -1) {
          n = fis.read(buffer);
          if (n > 0) {
             digest.update(buffer, 0, n);
          }
       }
       return digest.digest();
    }*/
}
