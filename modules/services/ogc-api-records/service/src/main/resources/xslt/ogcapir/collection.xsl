<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html"
              media-type="text/html"
              encoding="UTF-8"
              indent="no"/>
  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
  <xsl:import href="classpath:xslt/core/themes/default/theme.xsl"/>

  <xsl:template match="/">
    <xsl:message><xsl:copy-of select="."/></xsl:message>

    <xsl:variable name="collection"
                  select="list/values"
                  as="node()?"/>

    <html>
      <xsl:attribute name="lang" select="$language"/>
      <xsl:call-template name="html-head">
        <xsl:with-param name="title" select="$collection/name"/>
      </xsl:call-template>
      <xsl:call-template name="html-body">
        <xsl:with-param name="title" select="$collection/name"/>
        <xsl:with-param name="content">
          <xsl:call-template name="html-breadcrumb">
            <xsl:with-param name="breadcrumb" select="$collection/filter"/>
          </xsl:call-template>
        </xsl:with-param>
      </xsl:call-template>
    </html>
  </xsl:template>
</xsl:stylesheet>