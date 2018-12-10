package group1.hw3.routing;

import java.util.Hashtable;

public interface INode<T extends IMessage> {
    String getClientID();
    void receiveUpdate(T message);
    boolean selfUpdate();
    Hashtable<String, String> getForwardingTable();
}
