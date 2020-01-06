package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.LoginSignupBLL;
import com.onlinevotingsystem.CandidateActivity;
import com.onlinevotingsystem.EditMemberActivity;
import com.onlinevotingsystem.EditPositionActivity;
import com.onlinevotingsystem.MemberActivity;
import com.onlinevotingsystem.PositionActivity;
import com.onlinevotingsystem.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import API.API;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Position;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    Context mContext;
    List<User> memberList;
    String path;

    public MemberAdapter(Context mContext, List<User> memberList) {
        this.mContext = mContext;
        this.memberList = memberList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_member, viewGroup, false);
        return new MemberAdapter.MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        final User user = memberList.get(i);
        final String imagePath = Reusable.BASE_URL+user.getImageName();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            memberViewHolder.imgMember.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        memberViewHolder.tvMemberName.setText(user.getName());

        memberViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                API Api = Reusable.getInstance().create(API.class);
                int id =user.getId();
                Call<User> listCall = Api.searchUser(token, id);

                listCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(mContext,response.body().toString(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, EditMemberActivity.class);
                        intent.putExtra("name", user.getName());
                        intent.putExtra("address", user.getAddress());
                        intent.putExtra("contact", user.getContact());
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("id", user.getId());
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(mContext, "Error ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        memberViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =user.getId();
                deleteMember(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder{
        TextView tvMemberName;
        Button btnEdit, btnDelete;
        CircleImageView imgMember;

        public MemberViewHolder(@NonNull View memberView) {
            super(memberView);
            tvMemberName= memberView.findViewById(R.id.tvMemberName);
            imgMember = memberView.findViewById(R.id.imgMemberPic);
            btnEdit= memberView.findViewById(R.id.btnEdit);
            btnDelete= memberView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteMember(final int id) {
        new AlertDialog.Builder(mContext).setTitle("Delete Member!").setMessage("Do you really want Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){
                        final LoginSignupBLL bll = new LoginSignupBLL();
                        StrictMode();
                        SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                        String token = sharedPreference.getString("token","");
                        if (bll.deleteUser(token, id)){
                            Toast.makeText(mContext, "Successful Deleted;", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, MemberActivity.class);
                            mContext.startActivity(intent);
                            ((MemberActivity)mContext).finish();
                        }
                        else {
                            Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();

    }
}
