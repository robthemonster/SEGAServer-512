import SEGAMessages.GroupNotification;

public class SendGroupNotification implements Runnable {
    private GroupNotification groupNotification;

    public SendGroupNotification(GroupNotification groupNotification) {
        this.groupNotification = groupNotification;
    }

    @Override
    public void run() {
        FirebaseManager.sendGroupNotification(groupNotification);
    }
}
