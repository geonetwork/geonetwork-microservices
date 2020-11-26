<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-util="https://geonetwork-opensource.org/gn-util"
                exclude-result-prefixes="#all"
                version="3.0">


  <xsl:template name="html-field">
    <xsl:param name="label"/>
    <xsl:param name="text"/>

    <div class="w-1/3"><xsl:value-of select="$label"/></div>
    <div class="w-2/3 font-medium"><xsl:value-of select="$text"/></div>
  </xsl:template>
</xsl:stylesheet>