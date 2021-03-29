import com.sun.jdi.Value;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements the sender.
 */
public class MessageSender {
    // maximum transfer unit (frame length limit)
    private int mtu;
    // Source of the messages.
    private final Scanner stdin;
    private static final String startFrameDelimiter = "[";
    private static final String endFrameDelimiter = "]";
    public List<String> frames = new ArrayList<String>();

    private String segmentLength = "00";
    private String frameType = "F";
    private String fieldDelimiter = "~";


    /**
     * Create and initialize a new MessageSender.
     *
     * @param mtu the maximum transfer unit (MTU) (the length of a frame must
     *            not exceed the MTU)
     */
    public MessageSender(int mtu) {
        this.mtu = mtu;
        this.stdin = new Scanner(System.in);
    }

    /**
     * Read a line from standard input layer and break it into frames
     * that are output on standard output, one frame per line.
     * Report any errors on standard error.
     */
    public void sendMessage() {

        String message = stdin.nextLine();
        if (message != null) {

            if (((mtu - 10 <=0) &&(!message.equals("")))||((mtu-10< 0)&&(message.equals("")))){
                System.err.println("MTU is too small.");

                } else {


                if((message.length()>99)&&(mtu>109)){
                    mtu = 109;
                }
                //for the split
                if (message.length() > (mtu - 10)) {
                   frameMaker(message);

                    for (int i = 0; i < frames.size(); i++) {
                        System.out.println(frames.get(i));
                    }
                }

                //if it doesnt need to be split - output final message
                else {

                    System.out.println(frameMaker(message));

                }
            }
        } else {
            System.err.println("No message received.");
        }


    }




    public String frameMaker(String message) {
        segmentLength = getSegmentLength(message);
        String strCheckSum = getCheckSum(message);
        String finalCheckSum = strCheckSum.substring(1, 3);
        String preMessage = startFrameDelimiter + frameType + fieldDelimiter + segmentLength + fieldDelimiter + message + fieldDelimiter + finalCheckSum + endFrameDelimiter;

        return messageSplit(preMessage, message);
    }

    public String messageSplit(String entireFrame, String msg) {

        if (entireFrame.length() > mtu) {

            int totalSize = mtu - 10;
            String amountOfDots = "";
            frameType = "D";


            for (int x = 0; x < totalSize; x++) {
                amountOfDots += ".";
            }
            List<String> splitStringArray = Arrays.asList(msg.split("(?<=\\G" + amountOfDots + ")"));

            for (int i = 0; i < splitStringArray.size(); i++) {

                frames.add(frameMaker(splitStringArray.get(i)));
            }
            frames.remove(frames.size() - 1);
            frameType = "F";
            frames.add(frameMaker(splitStringArray.get(splitStringArray.size() - 1)));
        }
        //else
        return entireFrame;

    }


    //Checksum generated
    public String getCheckSum(String message) {
        int sum = 0;
        String arithmeticOfChars = frameType + fieldDelimiter + segmentLength + fieldDelimiter + message + fieldDelimiter;

        //convert to decimal
        for (int i = 0; i < arithmeticOfChars.length(); i++) {

            sum += (int) arithmeticOfChars.charAt(i);
        }

        if (Integer.toHexString(sum).length() > 3) {
            return Integer.toHexString(sum).substring(1, 4);
        }
        //convert to hex
        return Integer.toHexString(sum);
    }


    //length of msg
    public String getSegmentLength(String message) {
        String segmentLengthMsg = String.valueOf(message.length());
        if (message.length() < 10) {
            segmentLength = "0" + segmentLengthMsg;
            segmentLengthMsg = segmentLength;
        }
        return segmentLengthMsg;

    }
}

