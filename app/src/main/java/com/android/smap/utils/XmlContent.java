package com.android.smap.utils;

import java.io.InputStream;

/**
 * Created by kai on 5/04/2015.
 */
public class XmlContent {
    public static String xmlContent = "<h:html xmlns=\"http://www.w3.org/2002/xforms\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\">\n" +
            "  <h:head>\n" +
            "    <h:title>String only form</h:title>\n" +
            "    <model>\n" +
            "      <instance>\n" +
            "        <data id=\"build_String-only-form_1406426192\">\n" +
            "          <meta>\n" +
            "            <instanceID/>\n" +
            "          </meta>\n" +
            "          <textFieldVanilla/>\n" +
            "          <textFieldRequired/>\n" +
            "          <textFieldLength/>\n" +
            "        </data>\n" +
            "      </instance>\n" +
            "      <itext>\n" +
            "        <translation lang=\"eng\">\n" +
            "          <text id=\"/data/textFieldVanilla:label\">\n" +
            "            <value>Text Field Vanilla</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldVanilla:hint\">\n" +
            "            <value>no restrictions on this field</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldRequired:label\">\n" +
            "            <value>Text Field Required</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldRequired:hint\">\n" +
            "            <value>this is a required field</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldRequired:constraintMsg\">\n" +
            "            <value>Text Field Required</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldLength:label\">\n" +
            "            <value>Text Field Length</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldLength:hint\">\n" +
            "            <value>Length &gt; 5 &amp;&amp; &lt; 10</value>\n" +
            "          </text>\n" +
            "          <text id=\"/data/textFieldLength:constraintMsg\">\n" +
            "            <value>Text Field Length</value>\n" +
            "          </text>\n" +
            "        </translation>\n" +
            "      </itext>\n" +
            "      <bind nodeset=\"/data/meta/instanceID\" type=\"string\" readonly=\"true()\" calculate=\"concat('uuid:', uuid())\"/>\n" +
            "      <bind nodeset=\"/data/textFieldVanilla\" type=\"string\"/>\n" +
            "      <bind nodeset=\"/data/textFieldRequired\" type=\"string\" required=\"true()\" jr:constraintMsg=\"jr:itext('/data/textFieldRequired:constraintMsg')\"/>\n" +
            "      <bind nodeset=\"/data/textFieldLength\" type=\"string\" constraint=\"(regex(., &quot;^.{5,10}$&quot;))\" jr:constraintMsg=\"jr:itext('/data/textFieldLength:constraintMsg')\"/>\n" +
            "    </model>\n" +
            "  </h:head>\n" +
            "  <h:body>\n" +
            "    <input ref=\"/data/textFieldVanilla\">\n" +
            "      <label ref=\"jr:itext('/data/textFieldVanilla:label')\"/>\n" +
            "      <hint ref=\"jr:itext('/data/textFieldVanilla:hint')\"/>\n" +
            "    </input>\n" +
            "    <input ref=\"/data/textFieldRequired\">\n" +
            "      <label ref=\"jr:itext('/data/textFieldRequired:label')\"/>\n" +
            "      <hint ref=\"jr:itext('/data/textFieldRequired:hint')\"/>\n" +
            "    </input>\n" +
            "    <input ref=\"/data/textFieldLength\">\n" +
            "      <label ref=\"jr:itext('/data/textFieldLength:label')\"/>\n" +
            "      <hint ref=\"jr:itext('/data/textFieldLength:hint')\"/>\n" +
            "    </input>\n" +
            "  </h:body>\n" +
            "</h:html>\n";
    public static InputStream xmlInStream;

    public XmlContent() {
    }
}
