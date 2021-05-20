package in.codingloop.sms.objects;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import in.codingloop.sms.Constants;

public class BlockedSenders {
    private int id;
    private String blocked_sender;
    private int block_type;

    public BlockedSenders(int id, String blocked_sender, int block_type) {
        this.id = id;
        this.blocked_sender = blocked_sender;
        this.block_type = block_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlocked_sender() {
        return blocked_sender;
    }

    public void setBlocked_sender(String blocked_sender) {
        this.blocked_sender = blocked_sender;
    }

    public int getBlock_type() {
        return block_type;
    }

    public void setBlock_type(int block_type) {
        this.block_type = block_type;
    }

    public boolean shouldBlock(String sender) {
        if (block_type == Constants.BU_R1_EQUALS) {
            return sender.toLowerCase().equals(blocked_sender.toLowerCase());
        } else if (block_type == Constants.BU_R2_CONTAINS) {
            return sender.toLowerCase().contains(blocked_sender.toLowerCase());
        }
        return true;
    }

    public static boolean shouldSendSMS(String sender, List<BlockedSenders> blockList) {
        for (BlockedSenders bls: blockList) {
            if (bls.shouldBlock(sender)) return false;
        }
        return true;
    }

    public static List<BlockedSenders> getBlockedSendersFromCursor(Cursor cursor) {
        List<BlockedSenders> blockedSenders = new ArrayList<>();

        while (cursor.moveToNext()) {
            BlockedSenders bs = new BlockedSenders(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2)
            );
            blockedSenders.add(bs);
        }

        return blockedSenders;
    }
}
