package com.example.caustudy;

import android.os.Bundle;
import android.text.Spanned;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.commonmark.node.Block;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.util.Set;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.core.CorePlugin;

public class CoreActivity extends ActivityWithMenuOptions {

    private TextView textView;

    @NonNull
    @Override
    public MenuOptions menuOptions() {
        return MenuOptions.create()
                .add("simple", this::simple)
                .add("toast", this::toast)
                .add("alreadyParsed", this::alreadyParsed)
                .add("enabledBlockTypes", this::enabledBlockTypes);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);

        textView = findViewById(R.id.text_view);

        simple();

    }

    private void step_1() {

        // short call
        final Markwon markwon = Markwon.create(this);

        // this is the same as calling
        final Markwon markwon2 = Markwon.builder(this)
                .usePlugin(CorePlugin.create())
                .build();
    }

    /**
     * To simply apply raw (non-parsed) markdown call {@link Markwon#setMarkdown(TextView, String)}
     */
    private void simple() {

        // 여기에 그냥 친거 그대로 받아오면 될 것 같은데?
        final String markdown = "Hello **markdown**!";

        final Markwon markwon = Markwon.create(this);

        // this will parse raw markdown and set parsed content to specified TextView
        markwon.setMarkdown(textView, markdown);
    }


    private void toast() {

        final String markdown = "*Toast* __here__!\n\n> And a quote!";

        final Markwon markwon = Markwon.create(this);

        final Spanned spanned = markwon.toMarkdown(markdown);

        Toast.makeText(this, spanned, Toast.LENGTH_LONG).show();
    }

    /**
     * To apply already parsed markdown use {@link Markwon#setParsedMarkdown(TextView, Spanned)}
     */
    private void alreadyParsed() {

        final String markdown = "This **is** pre-parsed [markdown](#)";

        final Markwon markwon = Markwon.create(this);

        // parse markdown to obtain a Node
        final Node node = markwon.parse(markdown);

        // create a spanned content from parsed node
        final Spanned spanned = markwon.render(node);

        // apply parsed markdown
        markwon.setParsedMarkdown(textView, spanned);
    }

    private void enabledBlockTypes() {

        final String md = "" +
                "# Head\n\n" +
                "> and disabled quote\n\n" +
                "```\n" +
                "yep\n" +
                "```";

        final Set<Class<? extends Block>> blocks = CorePlugin.enabledBlockTypes();
        blocks.remove(BlockQuote.class);

        final Markwon markwon = Markwon.builder(this)
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureParser(@NonNull Parser.Builder builder) {
                        builder.enabledBlockTypes(blocks);
                    }
                })
                .build();

        markwon.setMarkdown(textView, md);
    }
}
