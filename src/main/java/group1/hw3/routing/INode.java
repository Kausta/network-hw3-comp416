package group1.hw3.routing;

import java.util.Hashtable;

public interface INode<T extends IMessage> {
    String getClientID();
    void receiveUpdate(T message);
    boolean sendUpdate();
    Hashtable<String, String> getForwardingTable();
}
