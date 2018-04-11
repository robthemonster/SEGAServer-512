import SEGAMessages.Request;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.err.println("BOOT");
        try {
            Thread ftpServer = getSftpServerThread();
            Thread messageHandler = getMessageHandlerThread();
            Logger.startLogger(System.currentTimeMillis());
            Logger.debug("Logger started");
            messageHandler.setPriority(Thread.MAX_PRIORITY);
            ftpServer.setPriority(Thread.NORM_PRIORITY);
            ftpServer.start();
            messageHandler.start();
            Logger.debug("SEGA SERVER HAS BEGUN");
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());
        }
    }

    private static Thread getSftpServerThread() {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(6921);
        File hostKeys = new File("privatekey" + File.separator + "keygen" + File.separator + "hostkey.ser");
        if (!hostKeys.getParentFile().exists()) {
            hostKeys.getParentFile().mkdir();
        }
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKeys));
        sshServer.setCommandFactory(new ScpCommandFactory());
        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>();
        userAuthFactories.add(new UserAuthPasswordFactory());
        sshServer.setUserAuthFactories(userAuthFactories);
        sshServer.setPasswordAuthenticator((groupname, token, serverSession) -> DatabaseManager.matchesToken(groupname, token));
        File home = new File(System.getProperty("user.dir"));
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(home.toPath()));
        List<NamedFactory<Command>> commandFactories = new ArrayList<>();
        commandFactories.add(new SftpSubsystemFactory());
        sshServer.setSubsystemFactories(commandFactories);
        return new Thread(() -> {
            try {
                sshServer.start();
            } catch (IOException e) {
                Logger.debug(e.getMessage());
            }
        });
    }

    private static Thread getMessageHandlerThread() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream("privatekey" + File.separator + "segastore.jks"), "gottagofast".toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, "gottagofast".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(6969);
        serverSocket.setWantClientAuth(false);
        serverSocket.setNeedClientAuth(false);
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
                RequestRunnable requestRunnable = new RequestRunnable((Request) object);
                new Thread(requestRunnable).start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());
        }
    }

}

