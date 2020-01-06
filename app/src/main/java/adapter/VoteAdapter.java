package adapter;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.onlinevotingsystem.BLL.VoteBLL;
import com.onlinevotingsystem.R;
import com.onlinevotingsystem.VoteActivity;
import com.onlinevotingsystem.VotingActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import createChannel.CreateChannel;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Vote;
import reusable.Reusable;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {
    Context mContext;
    List<Vote> votingCandidateList;
    String token;
    int candidate_id, pos_id, voter_id;
    NotificationManagerCompat notificationManagerCompat;

    public VoteAdapter(Context mContext, List<Vote> votingCandidateList) {
        this.mContext = mContext;
        this.votingCandidateList = votingCandidateList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_vote, viewGroup, false);
        return new VoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder voteViewHolder, int i) {
        final Vote voteCandidate = votingCandidateList.get(i);
        voteViewHolder.tvCandidateName.setText("Name: "+voteCandidate.getUser_name()+"\n Position : "+voteCandidate.getPosition_name());

        final String imagePath = Reusable.BASE_URL+voteCandidate.getPath();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            voteViewHolder.imgVoter.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        voteViewHolder.btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                notificationManagerCompat = NotificationManagerCompat.from(mContext);
                CreateChannel channel = new CreateChannel(mContext);
                channel.createChannel();
                SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                voter_id = sharedPreference.getInt("id", 0);
                pos_id = voteCandidate.getPosition_id();
                candidate_id = voteCandidate.getId();
                String token = sharedPreference.getString("token","");

                final VoteBLL bll = new VoteBLL();
                StrictMode();
                if (bll.castVote(token, candidate_id)){
                    Toast.makeText(mContext, "Voting Successful.", Toast.LENGTH_LONG).show();

                    final VoteBLL vbll = new VoteBLL( pos_id, candidate_id, voter_id);
                    if (vbll.castVoteHistory(token)){
                        displayNotification1();
                        displaynotificationWatch();
                        Intent intent = new Intent(mContext, VoteActivity.class);
                        mContext.startActivity(intent);
                        ((VotingActivity)mContext).finish();
                    }
                    else {
                        Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return votingCandidateList.size();
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder{
        TextView tvCandidateName;
        Button btnVote;
        CircleImageView imgVoter;

        public VoteViewHolder(@NonNull View voteCandidateView) {
            super(voteCandidateView);
            tvCandidateName = voteCandidateView.findViewById(R.id.tvCandidateName);
            btnVote = voteCandidateView.findViewById(R.id.btnVote);
            imgVoter = voteCandidateView.findViewById(R.id.imgVoter);
        }

    }

    private void displayNotification1() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_vote)
                .setContentTitle("E-Voting")
                .setContentText("Voting Successful.")
                .extend(new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        // Issue the notification with notification manager.
        notificationManager.notify(1, builder.build());

    }

    private void displaynotificationWatch(){
        Intent intent = new Intent(mContext, VoteActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_vote,
                "E-voting",
                pendingIntent).build();

        Notification notification = new NotificationCompat.Builder(mContext)
                .setContentTitle("E-Voting")
                .setContentText("Voting Successful.")
                .setSmallIcon(R.drawable.ic_vote)
                .extend(new NotificationCompat.WearableExtender().addAction(action))
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(001, notification);
    }
}
