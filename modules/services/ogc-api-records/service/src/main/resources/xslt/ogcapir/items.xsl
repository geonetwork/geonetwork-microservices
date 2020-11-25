<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>
  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>

  <xsl:template match="/">
    <xsl:message><xsl:copy-of select="."/></xsl:message>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$collection/name"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="title" select="$collection/name"/>
        <xsl:with-param name="content">


          <section class="bg-white border-b py-8">
            <div class="container mx-auto flex flex-wrap pt-4 pb-12">

              Items
            </div>
          </section>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>