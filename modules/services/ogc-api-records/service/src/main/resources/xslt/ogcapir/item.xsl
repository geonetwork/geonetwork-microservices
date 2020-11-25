<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>
  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>

  <xsl:template match="/">
    <xsl:message>
      <xsl:copy-of select="."/>
    </xsl:message>

    <xsl:variable name="item"
                  select="model/items/item"/>
    <xsl:variable name="uuid"
                  select="$item/uuid"
                  as="xs:string"/>
    <xsl:variable name="metadata"
                  select="parse-xml($item/xml)"
                  as="node()"/>
    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$uuid"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="title" select="$uuid"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$uuid"/>
          </xsl:call-template>


          <section class="bg-white border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12">

              <pre class="text-gray-900">
                <xsl:copy-of select="$item/xml"/>
              </pre>
            </div>
          </section>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>