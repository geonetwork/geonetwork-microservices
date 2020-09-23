<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                version="3.0"
                exclude-result-prefixes="#all">


  <xsl:template name="get-languages">
    <xsl:param name="metadata" as="node()*"/>

    <xsl:variable name="mainLanguageCode" as="xs:string?"
                  select="$metadata/gmd:language[1]/*/
                              @codeListValue[normalize-space(.) != '']"/>

    <xsl:variable name="mainLanguage" as="xs:string?"
                  select="if ($mainLanguageCode)
                            then $mainLanguageCode
                            else $metadata/gmd:language[1]/gco:CharacterString[normalize-space(.) != '']"/>

    <xsl:variable name="otherLanguages" as="attribute()*"
                  select="$metadata/gmd:locale/*/gmd:languageCode/*/
                              @codeListValue[normalize-space(.) != '']"/>

    <lang id="default" code="{$mainLanguage}"/>
    <xsl:for-each select="$otherLanguages">
      <lang id="{../../../@id}" code="{.}"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>