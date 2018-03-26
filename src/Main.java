import SEGAMessages.*;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("SEGA SERVER HAS BEGUN");

        try {
            Thread ftpServer = getFtpServerThread();
            Thread messageHandler = getMessageHandlerThread();
            Logger.startLogger(System.currentTimeMillis());
            Logger.debug("Logger started");
            messageHandler.setPriority(Thread.MAX_PRIORITY);
            ftpServer.setPriority(Thread.NORM_PRIORITY);
            ftpServer.start();
            messageHandler.start();

        } catch (IOException | FtpException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());

        }
    }

    private static Thread getFtpServerThread() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(true);
        connectionConfigFactory.setMaxThreads(10);
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfigurationFactory.setPassivePorts("6921-6931");
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(6921);
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        BaseUser anon = new BaseUser();
        anon.setName("anon");
        anon.setHomeDirectory(System.getProperty("user.dir"));
        anon.setAuthorities(Arrays.asList(new WritePermission(), new ConcurrentLoginPermission(10, 1)));
        TreeMap<String, Listener> listenerTreeMap = new TreeMap<>();
        listenerTreeMap.put("default", listenerFactory.createListener());
        serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
        serverFactory.setListeners(listenerTreeMap);
        serverFactory.getUserManager().save(anon);
        return new Thread(() -> {
            try {
                FtpServer server = serverFactory.createServer();
                server.start();
                while (!server.isStopped()) {
                    if (server.isSuspended()) {
                        System.out.println("ftp suspended");
                    }
                }

            } catch (FtpException e) {
                e.printStackTrace();
                Logger.debug(e.getMessage());
            }
        });
    }

    private static Thread getMessageHandlerThread() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(6969);
        return new Thread(() -> {
            try {
                Socket socket;
                while (true) {
                    socket = serverSocket.accept();
                    handleMessage(socket);
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.debug(e.getMessage());
            }
        });
    }

    private static void handleMessage(Socket socket) {
        try {
            ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
            Object object = stream.readObject();
            if (object instanceof Request) {
                RequestRunnable requestRunnable = getRequestRunnable((Request) object);
                new Thread(requestRunnable).start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());
        }
    }

    public static RequestRunnable getRequestRunnable(Request request) {
        if (request instanceof CreateUserRequest) {
            return new CreateUserRunnable((CreateUserRequest) request);
        }
        if (request instanceof UserLoginRequest) {
            return new UserLoginRunnable((UserLoginRequest) request);
        }
        if (request instanceof CreateGroupRequest) {
            return new CreateGroupRunnable((CreateGroupRequest) request);
        }
        if (request instanceof GetGroupsForUserRequest) {
            return new GetGroupsForUserRunnable((GetGroupsForUserRequest) request);
        }
        if (request instanceof GetUsersForGroupRequest) {
            return new GetUsersForGroupRunnable((GetUsersForGroupRequest) request);
        }
        if (request instanceof RequestAuthorizationFromGroupRequest) {
            return new RequestAuthorizationFromGroupRunnable((RequestAuthorizationFromGroupRequest) request);
        }
        if (request instanceof GrantAuthorizationForGroupRequest) {
            return new GrantAuthorizationForGroupAccessRunnable((GrantAuthorizationForGroupRequest) request);
        }
        if (request instanceof AddUserToGroupRequest) {
            return new AddUserToGroupRunnable((AddUserToGroupRequest) request);
        }
        if (request instanceof GetFilesForGroupRequest) {
            return new GetFilesForGroupRunnable((GetFilesForGroupRequest) request);
        }
        if (request instanceof DeleteFileFromGroupRequest) {
            return new DeleteFileFromGroupRunnable((DeleteFileFromGroupRequest) request);
        }
        return null;
    }
}

