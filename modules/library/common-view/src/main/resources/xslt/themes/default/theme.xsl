<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0">
  <xsl:output method="html" indent="no"/>

  <xsl:variable name="html-head" select="document('head.html')" as="node()"/>
  <xsl:variable name="html-nav" select="document('nav.html')" as="node()"/>
  <xsl:variable name="html-footer" select="document('footer.html')" as="node()"/>

  <xsl:template name="html-head">
    <xsl:copy-of select="$html-head"/>
    <!-- TODO: inject title -->
  </xsl:template>

  <xsl:template name="html-body">
    <xsl:param name="content" as="node()*"/>

    <body class="leading-normal tracking-normal text-white gradient">
      <xsl:copy-of select="$html-nav"/>

      <xsl:copy-of select="$content"/>

      <xsl:copy-of select="$html-footer"/>
    </body>
  </xsl:template>
</xsl:stylesheet>