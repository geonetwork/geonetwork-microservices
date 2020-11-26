<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>

  <xsl:import href="../commons/xsl-params-core.xsl"/>
  <xsl:import href="../themes/default/theme.xsl"/>

  <xsl:template match="/">
    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head"/>
      <xsl:call-template name="html-body">
        <xsl:with-param name="content">
          <div class="pt-24">
            <div class="container px-3 mx-auto flex flex-wrap flex-col md:flex-row items-center">
              <div class="flex flex-col w-full md:w-2/5 justify-center items-start text-center md:text-left">
                <xsl:for-each select="error">
                  <h1 class="my-4 text-5xl font-bold leading-tight">
                    Accessing <xsl:value-of select="path"/> in error (status <xsl:value-of select="status"/>)
                  </h1>
                  <p class="leading-normal text-2xl mb-8">
                    <xsl:value-of select="message"/>
                  </p>
                  <xsl:if test="stackTrace != ''">
                    <p class="text-xs tracking-tighter">
                      <h2>Details:</h2>
                      <xsl:value-of select="stackTrace"/>
                    </p>
                  </xsl:if>
                </xsl:for-each>
              </div>

              <div class="w-full md:w-3/5 py-6 text-center">
              </div>
            </div>
          </div>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>