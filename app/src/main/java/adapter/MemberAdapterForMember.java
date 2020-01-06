package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.onlinevotingsystem.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.User;
import reusable.Reusable;

public class MemberAdapterForMember extends RecyclerView.Adapter<MemberAdapterForMember.MemberViewHolderForMember>{
    Context mContext;
    List<User> memberList;

    public MemberAdapterForMember(Context mContext, List<User> memberList) {
        this.mContext = mContext;
        this.memberList = memberList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public MemberViewHolderForMember onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_member_for_member, viewGroup, false);
        return new MemberAdapterForMember.MemberViewHolderForMember(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolderForMember memberViewHolderForMember, int i) {
        final User user = memberList.get(i);
        final String imagePath = Reusable.BASE_URL+user.getImageName();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            memberViewHolderForMember.imgMember.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        memberViewHolderForMember.tvMemberName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MemberViewHolderForMember extends RecyclerView.ViewHolder{
        TextView tvMemberName;
        CircleImageView imgMember;

        public MemberViewHolderForMember(@NonNull View memberView) {
            super(memberView);
            tvMemberName= memberView.findViewById(R.id.tvMemberName);
            imgMember = memberView.findViewById(R.id.imgMemberPic);
        }
    }
}
