package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import marquez.physicsops.Classes.DBSQLite;
import marquez.physicsops.Classes.FacadeSQLite;
import marquez.physicsops.Classes.Friend;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

/**
 * Created by Carlos-Torre on 13/05/2018.
 */

public class RVAdapterFriends  extends RecyclerView.Adapter<RVAdapterFriends.FriendViewHolder> {

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView nameFriend;
        RelativeLayout layout;
        ImageButton deleteFriend;
        ImageButton showFriend;
        Friend user;
        FriendViewHolder(View itemView, final Friend user) {
            super(itemView);
            nameFriend = (TextView) itemView.findViewById(R.id.titleActualFriend);
            deleteFriend = (ImageButton) itemView.findViewById(R.id.buttonDeleteFriend);
            showFriend = (ImageButton)  itemView.findViewById(R.id.buttonShowFriend);
            layout = (RelativeLayout) itemView.findViewById(R.id.layoutRvFriendRequest);
            this.user = user;
        }
    }
    User user;
    List<Friend> friends;
    FriendshipActivity activity;

    RVAdapterFriends(User user, List<Friend> friends, FriendshipActivity activity){
        this.user = user;
        this.friends = friends;
        this.activity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RVAdapterFriends.FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_actual_friends, viewGroup, false);
        RVAdapterFriends.FriendViewHolder pvh = new RVAdapterFriends.FriendViewHolder(v, friends.get(i));
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RVAdapterFriends.FriendViewHolder userViewHolder, final int i) {
        if(user.getUserName().equals(friends.get(i).getIdUserFrom()))
            userViewHolder.nameFriend.setText(friends.get(i).getIdUserTo());
        else
            userViewHolder.nameFriend.setText(friends.get(i).getIdUserFrom());

        userViewHolder.deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CardLogFriend",friends.get(i).toString() +"Deleted");
                friends.get(i).setState("deleted");
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.scale_down);
                userViewHolder.layout.startAnimation(animation);
                Toast.makeText(v.getContext(), "Se ha eliminado a " + friends.get(i).getIdUserFrom(), Toast.LENGTH_SHORT).show();

                DBSQLite db = new DBSQLite(v.getContext(), v.getResources().getString(R.string.nameDB), null, v.getResources().getInteger(R.integer.versionSQLite));
                FacadeSQLite sql = new FacadeSQLite(db);
                sql.deleteFriend(user, friends.get(i));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.resetRecyclerView();
                    }
                }, 290);

            }
        });
        userViewHolder.showFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showFriendIntent = new Intent(activity.getApplicationContext(), ShowFriendActivity.class);
                activity.startActivity(showFriendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}