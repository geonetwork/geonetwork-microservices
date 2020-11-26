<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-util="https://geonetwork-opensource.org/gn-util"
                exclude-result-prefixes="#all"
                version="3.0">

  <xsl:function name="gn-util:getCollectionName" as="xs:string">
    <xsl:param name="collection" as="element(collection)"/>
    <xsl:param name="language" as="xs:string"/>

    <xsl:value-of select="$collection/(
                            labelTranslations/entry[key = $language3letters]/value[. != '']
                            |name[. != '']
                            |uuid)[1]"/>
  </xsl:function>

  <xsl:function name="gn-util:getCollectionLogo" as="xs:string">
    <xsl:param name="collection" as="element(collection)"/>

    <xsl:value-of select="if ($collection/type = ('harvester', 'portal'))
                          then concat($base, $logoFolder, '/', $collection/uuid, '.png')
                          else concat($base, $harvestingFolder, '/', $collection/logo)"/>
  </xsl:function>
</xsl:stylesheet>