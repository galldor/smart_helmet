package br.com.fiap.smarthelmet;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GraphFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FirebaseGraph";
    private static final Integer MAX_GRAPH = 150;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineGraphSeries<DataPoint> lineGraphSeries;
    private int lastX = 0;
    private DatabaseReference databaseReference;
    private OnFragmentInteractionListener mListener;
    private List<Double> listGraphValues;

    public GraphFragment() {
        // Required empty public constructor
    }

    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lineGraphSeries = new LineGraphSeries<>();
        listGraphValues = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        final GraphView graph = (GraphView) view.findViewById(R.id.graph);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(MAX_GRAPH);
        viewport.setScrollable(true);
        initLineGraphSeries();
        graph.addSeries(lineGraphSeries);


        databaseReference = ((MainActivity) getActivity()).getDatabaseReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double impactValue = dataSnapshot.getValue(Double.class);
                Log.d(TAG, "value is: " + dataSnapshot.getValue(Double.class));
                Double lastValue = listGraphValues.isEmpty() ? 0 : listGraphValues.get(listGraphValues.size() - 1);
                if(impactValue > 1.0 && lastValue != impactValue) {
                    addEntry(impactValue);
                    graph.addSeries(lineGraphSeries);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return view;
    }

    private void initLineGraphSeries(){
        List<Double> databaseValues = getDatabaseValues();
        databaseValues.remove(databaseValues.size() - 1);
        for(Double value : getDatabaseValues()){
            addEntry(value);
        }
    }

    private List<Double> getDatabaseValues() {
        return ((MainActivity) getActivity()).getFirabaseValues();
    }

    private void addEntry(Double entryValue) {
        lineGraphSeries.appendData(new DataPoint(lastX++, entryValue), true, MAX_GRAPH);
        listGraphValues.add(entryValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
