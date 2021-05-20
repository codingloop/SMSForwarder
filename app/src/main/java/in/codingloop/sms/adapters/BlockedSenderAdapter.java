package in.codingloop.sms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.codingloop.sms.ActionInterface;
import in.codingloop.sms.R;
import in.codingloop.sms.objects.BlockedSenders;

public class BlockedSenderAdapter extends RecyclerView.Adapter<BlockedSenderAdapter.BlockedSenderView> {
    ActionInterface ai;
    List<BlockedSenders> blockedSendersList;

    public BlockedSenderAdapter(ActionInterface ai, List<BlockedSenders> blockedSendersList) {
        this.ai = ai;
        this.blockedSendersList = blockedSendersList;
    }

    @NonNull
    @Override
    public BlockedSenderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_blocked_sender,
                parent,
                false
        );
        return new BlockedSenderView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedSenderView holder, int position) {
        BlockedSenders blockedSender = blockedSendersList.get(position);
        holder.sender_name.setText(blockedSender.getBlocked_sender());

        String hint_text = "Blocked messages from " + blockedSender.getBlocked_sender();
        if (blockedSender.getBlock_type() == 2) {
            hint_text = "Blocked if Sender name contains " + blockedSender.getBlocked_sender();
        }
        holder.relation_hint.setText(hint_text);

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ai.deleteBlockedSender(blockedSender.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockedSendersList.size();
    }

    public class BlockedSenderView extends RecyclerView.ViewHolder {
        TextView sender_name, relation_hint;
        Button delete_button;

        public BlockedSenderView(@NonNull View itemView) {
            super(itemView);
            sender_name = itemView.findViewById(R.id.r_bs_tv_contact);
            relation_hint = itemView.findViewById(R.id.r_bs_tv_hint_text);
            delete_button = itemView.findViewById(R.id.r_bs_b_delete);
        }
    }
}
