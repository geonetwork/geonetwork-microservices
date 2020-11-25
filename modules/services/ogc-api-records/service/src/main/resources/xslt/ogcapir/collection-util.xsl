<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-util="https://geonetwork-opensource.org/gn-util"
                version="3.0">

  <xsl:function name="gn-util:getCollectionName" as="xs:string">
    <xsl:param name="collection" as="element(values)"/>
    <xsl:param name="language" as="xs:string"/>

    <xsl:value-of select="$collection/(
                            labelTranslations/entry[key = $language]/value[. != '']
                            |name[. != '']
                            |uuid)[1]"/>
  </xsl:function>
</xsl:stylesheet>