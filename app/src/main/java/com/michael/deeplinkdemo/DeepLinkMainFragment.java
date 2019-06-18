package com.michael.deeplinkdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

public class DeepLinkMainFragment extends Fragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_deep_link_main, container, false);

        view.findViewById(R.id.btnToSettingsFragment).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("params", "from DeepLinkMainFragment");
                Navigation.findNavController(v).navigate(R.id.action_deepLinkMainFragment_to_deepLinkSettingsFragment, bundle);
            }
        });

        view.findViewById(R.id.sendNotification).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendNotification();
            }
        });

        return view;
    }

    /**
     * 通过PendingIntent设置，当通知被点击后需要跳转到哪个destination，以及传递的参数
     * */
    private PendingIntent getPendingIntent()
    {
        if(getActivity() != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("params", "from Notification");
            return Navigation
                    .findNavController(getActivity(), R.id.sendNotification)
                    .createDeepLink()
                    .setGraph(R.navigation.graph_deep_link_activity)
                    .setDestination(R.id.deepLinkSettingsFragment)
                    .setArguments(bundle)
                    .createPendingIntent();
        }
        return null;
    }

    private static final String CHANNEL_ID = "1";
    private static final int notificationId = 8;

    /**
     * 向通知栏发送一个通知
     * */
    private void sendNotification()
    {
        if(getActivity() == null)
        {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ChannelName", importance);
            channel.setDescription("description");
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("DeepLinkDemo")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        notificationManager.notify(notificationId, builder.build());
    }
}
