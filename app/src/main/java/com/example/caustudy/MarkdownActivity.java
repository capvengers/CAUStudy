package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.example.caustudy.R;

import org.commonmark.node.Emphasis;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.util.Collections;
import java.util.concurrent.Executors;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.LinkResolverDef;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.RenderProps;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.core.spans.StrongEmphasisSpan;
import io.noties.markwon.editor.AbstractEditHandler;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.editor.MarkwonEditorUtils;
import io.noties.markwon.editor.PersistedSpans;
import io.noties.markwon.image.AsyncDrawableScheduler;

public class MarkdownActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);
        editText = findViewById(R.id.editText);

        final Markwon markwon = Markwon.create(this);
        final MarkwonEditor editor = MarkwonEditor.create(markwon);



        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
        editText.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                editText));

        editor.process(editText.getText());


        editor.preRender(editText.getText(), new MarkwonEditor.PreRenderResultListener() {
            @Override
            public void onPreRenderResult(@NonNull MarkwonEditor.PreRenderResult result) {
                // it's wise to check if rendered result is for the same input,
                // for example by matching raw input
                if (editText.getText().toString().equals(result.resultEditable().toString())) {

                    // if you are in background thread do not forget
                    // to execute dispatch in main thread
                    result.dispatchTo(editText.getText());
                }
            }
        });

    }
}
