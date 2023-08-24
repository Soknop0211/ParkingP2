package com.daikou.p2parking.ui.change_language;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daikou.p2parking.R;
import com.daikou.p2parking.helper.HelperUtil;
import com.daikou.p2parking.utility.RedirectClass;

import java.util.List;

public class ChangeLanguageFragment extends DialogFragment {

    private InitListener initListener;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStyle(DialogFragment.STYLE_NORMAL, R.style.AlertShape);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_language_alert, container, false);
        progressBar = root.findViewById(R.id.progressItem);
        progressBar.setVisibility(View.GONE);

        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }

        gotoScreenLanguage(root);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = 16;
            InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), margin);
            dialog.getWindow().setBackgroundDrawable(inset);
        }
    }

    private void gotoScreenLanguage(View root){
        root.findViewById(R.id.txtCancel).setOnClickListener(view -> dismiss());
        RecyclerView recyclerViewLang = root.findViewById(R.id.recyclerViewLang);
        recyclerViewLang.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<LanguageModel> languageModelList = LanguageModel.getLanguage(requireContext());
        ChangeLangAdapter changeLangAdapter = new ChangeLangAdapter(requireContext(), languageModelList, clickCallBackListener);
        recyclerViewLang.setAdapter(changeLangAdapter);
    }

    private final ChangeLangAdapter.ClickCallBackListener clickCallBackListener = (pos, key,context) -> {
        String lang;
        if (pos == 0) {
            lang = "en";
        } else {
            lang = "kh";
        }

        HelperUtil.INSTANCE.changeLanguage(requireActivity(),  lang);

        progressBar.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (initListener != null) {
                dismiss();
                // initListener.initCallBack();
                RedirectClass.INSTANCE.gotoMainActivity(requireActivity());
            }
        }, 1500);

    };

    public void setInitListener(InitListener initListener) {
        this.initListener = initListener;
    }

    public interface InitListener {
        void initCallBack();
    }


}