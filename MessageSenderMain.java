/**
 * Main class for running the MessageSender.
 * The MTU value is received via the command-line arguments.
 * 
 * @author djb
 */
public class MessageSenderMain
{
    public static void main(String[] args)
    {
        int mtu = Integer.MIN_VALUE;
        int argnum = 0;
        boolean argsOk = true;
        while(argnum < args.length) {
            switch(args[argnum]) {
                case "--mtu":
                    argnum++;
                    if(argnum < args.length) {
                        mtu = Integer.parseInt(args[argnum]);
                        argnum++;
                    }
                    else {
                        System.err.println("Illegal MTU: " + args[argnum]);
                        argsOk = false;
                    }
                    break;
                case "<":
                    argnum++;
                default:
                    System.err.println("Unknown argument: " + args[argnum]);
                    argsOk = false;
            }
        }
        if(!argsOk) {
            
        }
        else if(mtu == Integer.MIN_VALUE) {
            System.err.println("No MTU specified.");
        }
        else {
            try {
                MessageSender sender = new MessageSender(mtu);
                sender.sendMessage();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        System.out.flush();
        System.err.flush();
    }


}
