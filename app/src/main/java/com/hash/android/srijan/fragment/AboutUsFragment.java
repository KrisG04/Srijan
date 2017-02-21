package com.hash.android.srijan.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hash.android.srijan.R;

/**
 * Created by Spandita Ghosh on 2/20/2017.
 */
public class AboutUsFragment extends android.support.v4.app.Fragment {
    public AboutUsFragment() {
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("About Us");
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_us_fragment, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.aboutUstextView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<p>Jadavpur University has always been an epitome of excellence and the perfect example of a place meant for holistic development of an individual. Ever since 1955, JU has been leading the front, be it for technical expertise or cultural prowess.</p><p>However, some time back in 2007, few individuals of the Faculty of Engineering and Technology at Jadavpur University grew restless thinking of how JU despite being an ocean of scientific mastery, was not contributing towards a platform for technical recreation. Every body could do classes, attend labs, have an \'adda\' at the field, crack complex research problems, or make merry dancing to songs at a fest. But as a leading technical institution, how was JU enabling others to explore their technical selves through recreation?</p><p>Thus, SRIJAN was conceived. :)</p><p>SRIJAN has always dedicated itself to the idea of promoting, showcasing, and encouraging concepts and research beyond the known periphery. Oddities are cheered, out-of-the-box thinking is lauded, and most importantly a platform is given to translate technical knowledge into realistic applications. SRIJAN has in its objectives to reflect the respect Jadavpur University commands in form of the talent supportive acts it anchors.</p><p>Today, SRIJAN stands proudly as the biggest and most prestigious techno-management fest in the City of Joy. Being a part of SRIJAN is like working at a startup: there are no fixed methods of working, you do not know whether your experiments will succeed, everyone has to do all kinds of work, you have to be bonded strongly to your teammates, and the learning and exposure is immense! :)</p><p>1955 to 2007 might have been a long time for SRIJAN to come into being, but 2007 to 2017 have been years of putting together blood and sweat to establish our beloved techno-management fest. Our track record is not a license to rest, but a challenge from our predecessors which we gladly accept. A shout-out to all our lovely seniors who worked day in and day out to make SRIJAN the favorite destination for all the dreamers and believers !</p><p>A month away from SRIJAN 2017, today we retrospect about what has gone and introspect about what is to come. With arms wide open, we invite everyone to come join us for 3 days of innovation, fun, recreation, brainstorming, productivity, and merry! We invite you to F.E.T.S.U. presents SRIJAN 2017.</p><p>24th, 25th, and 26th of February, 2017. Mark the dates.</p><p>SRIJAN is not just a fest for us. It is a Religion.</p>", Html.FROM_HTML_MODE_COMPACT));

        } else {
            textView.setText(Html.fromHtml("<p>Jadavpur University has always been an epitome of excellence and the perfect example of a place meant for holistic development of an individual. Ever since 1955, JU has been leading the front, be it for technical expertise or cultural prowess.</p><p>However, some time back in 2007, few individuals of the Faculty of Engineering and Technology at Jadavpur University grew restless thinking of how JU despite being an ocean of scientific mastery, was not contributing towards a platform for technical recreation. Every body could do classes, attend labs, have an \'adda\' at the field, crack complex research problems, or make merry dancing to songs at a fest. But as a leading technical institution, how was JU enabling others to explore their technical selves through recreation?</p><p>Thus, SRIJAN was conceived. :)</p><p>SRIJAN has always dedicated itself to the idea of promoting, showcasing, and encouraging concepts and research beyond the known periphery. Oddities are cheered, out-of-the-box thinking is lauded, and most importantly a platform is given to translate technical knowledge into realistic applications. SRIJAN has in its objectives to reflect the respect Jadavpur University commands in form of the talent supportive acts it anchors.</p><p>Today, SRIJAN stands proudly as the biggest and most prestigious techno-management fest in the City of Joy. Being a part of SRIJAN is like working at a startup: there are no fixed methods of working, you do not know whether your experiments will succeed, everyone has to do all kinds of work, you have to be bonded strongly to your teammates, and the learning and exposure is immense! :)</p><p>1955 to 2007 might have been a long time for SRIJAN to come into being, but 2007 to 2017 have been years of putting together blood and sweat to establish our beloved techno-management fest. Our track record is not a license to rest, but a challenge from our predecessors which we gladly accept. A shout-out to all our lovely seniors who worked day in and day out to make SRIJAN the favorite destination for all the dreamers and believers !</p><p>A month away from SRIJAN 2017, today we retrospect about what has gone and introspect about what is to come. With arms wide open, we invite everyone to come join us for 3 days of innovation, fun, recreation, brainstorming, productivity, and merry! We invite you to F.E.T.S.U. presents SRIJAN 2017.</p><p>24th, 25th, and 26th of February, 2017. Mark the dates.</p><p>SRIJAN is not just a fest for us. It is a Religion.</p>"));

        }
        return rootView;


    }
}
