package com.ainrif.apiator.doclet

import com.sun.source.doctree.CommentTree
import com.sun.source.doctree.EndElementTree
import com.sun.source.doctree.StartElementTree
import com.sun.source.doctree.TextTree
import com.sun.source.util.SimpleDocTreeVisitor

class TextDocTreeVisitor extends SimpleDocTreeVisitor<String, Void> {
    @Override
    String visitText(TextTree node, Void aVoid) {
        return node.body
    }

    @Override
    String visitComment(CommentTree node, Void aVoid) {
        return node.body
    }

    @Override
    String visitStartElement(StartElementTree node, Void aVoid) {
        return node.toString()
    }

    @Override
    String visitEndElement(EndElementTree node, Void aVoid) {
        return node.toString()
    }
}
