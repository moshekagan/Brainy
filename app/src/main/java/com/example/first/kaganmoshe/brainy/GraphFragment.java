package com.example.first.kaganmoshe.brainy;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.first.kaganmoshe.brainy.Setting.AppSettings;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import EEG.MindWave;
import Utils.Logs;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment implements IHeadSetData {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String GRAPH_FRAGMENT = "Graph_Fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Graph Data members
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private GraphView m_GraphView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logs.error("TEST", "in: GraphFragment.onCreate()");

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Get HeadSet - ic_mind_wave_mobile and register
        try{
            EegHeadSet headSet = AppManager.getInstance().getHeadSet();
            headSet.registerListener(this);
        } catch (Exception e) {
            // TODO - Not need to go hear never!!!!
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logs.error("TEST", "in: GraphFragment.onCreateView()");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        Logs.error("TEST", "GraphView graph = (GraphView) getView().findViewById(R.id.graph)");

        // we get graph view instance
        m_GraphView = (GraphView) view.findViewById(R.id.graph);

        Logs.error("TEST", "Finish:: ---GraphView graph = (GraphView) getView().findViewById(R.id.graph)");

        // data
        series = new LineGraphSeries<DataPoint>();
        m_GraphView.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = m_GraphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);

        return view;
    }

    @Override
    public void onDestroy() {
        Logs.info(GRAPH_FRAGMENT, Logs.SEPARATOR_LINE + "HeadSet Connection closed!" + Logs.SEPARATOR_LINE);
        super.onDestroy();
    }


    @Override
    public void onAttentionReceived(int attValue) {
        Logs.info(GRAPH_FRAGMENT, "Got Attention! " + EegHeadSet.ATTENTION_STR + ": " + attValue);

        final int att = attValue;

        getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addEntry(att);
                    Logs.debug(GRAPH_FRAGMENT, "Append to graph happen with value : " + att);
                }
            });
    }

    @Override
    public void onMeditationReceived(int medValue) {
        // Do nothing, currently we want to update the graph only with attention
        Logs.info(GRAPH_FRAGMENT, "Got Meditation! " + EegHeadSet.MEDITATION_STR + ": " + medValue);
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        // TODO - We need to discuss about it
//        String message = "";
//
//        switch (connectionState){
//            case DEVICE_CONNECTED:
//                message = "Connection established :)";
//                break;
//            case DEVICE_CONNECTING:
//                message = "Connecting...";
//                break;
//            case DEVICE_NOT_CONNECTED:
//                message = "Device disconnected! :(";
//                m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
//                break;
//            case DEVICE_NOT_FOUND:
//                message = "Device not found :(";
//                m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
//                break;
//            case BLUETOOTH_NOT_AVAILABLE:
//                message = "Bluetooth not available. Turn on bluetooth or check paring.";
//                m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
//                break;
//        }
//
//        Context context = getActivity().getApplicationContext();
//        CharSequence text = message;
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {
        // TODO -complite it
//        final ESignalVolume newSignalVolume = signalVolume;
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (newSignalVolume) {
//                    case HEAD_SET_NOT_COVERED:
////                        m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
////                        break;
//                    case POOR_SIGNAL_HIGH:
//                    case POOR_SIGNAL_LOW:
//                        m_ConnectivityIconImageV.setImageResource(R.drawable.medium);
//                        break;
//                    case GOOD_SIGNAL:
//                        m_ConnectivityIconImageV.setImageResource(R.drawable.good);
//                        break;
//                }
//            }
//        });
//
//        if (newSignalVolume == ESignalVolume.HEAD_SET_NOT_COVERED){
//            Context context = getActivity().getApplicationContext();
//            CharSequence text = "The Head set should be on the head... da!";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
    }

    private void addEntry(int value){
        // TODO - Here , we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, value), true, 20);
    }
}
