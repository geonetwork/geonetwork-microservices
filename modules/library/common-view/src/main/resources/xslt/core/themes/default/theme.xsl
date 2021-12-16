<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="#all"
                version="3.0">
  <xsl:output method="html" indent="no"/>

  <xsl:template name="html-head">
    <xsl:param name="title" select="''" as="xs:string?"/>


    <head>
      <meta charset="UTF-8"/>
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
      <title>
        <xsl:value-of select="$title"/>
      </title>
      <meta name="description" content=""/>
      <meta name="keywords" content=""/>
      <meta name="author" content=""/>
      <link rel="stylesheet" href="https://unpkg.com/tailwindcss@2.2.19/dist/tailwind.min.css"/>
      <!--Replace with your tailwind.css once created-->
      <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700"
            rel="stylesheet"/>
      <style>
        .clamp-1 {
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
        }

        .clamp-2 {
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .clamp-3 {
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
        }
      </style>

      <xsl:if test="/model/seoJsonLdSnippet">
      <script type="application/ld+json">
        <xsl:value-of select="/model/seoJsonLdSnippet"/>
      </script>
      </xsl:if>
    </head>
  </xsl:template>


  <xsl:template name="html-body">
    <xsl:param name="title" as="item()*"/>
    <xsl:param name="logo" as="item()*" required="false"/>
    <xsl:param name="link" as="xs:string?" required="false"/>
    <xsl:param name="content" as="node()*"/>

    <body class="leading-normal tracking-normal antialiased font-sans bg-white pt-16">
      <xsl:call-template name="header">
        <xsl:with-param name="title" select="$title"/>
        <xsl:with-param name="logo" select="$logo"/>
        <xsl:with-param name="link" select="$link"/>
      </xsl:call-template>

      <xsl:copy-of select="$content"/>

      <xsl:call-template name="footer"/>
    </body>
  </xsl:template>


  <xsl:template name="header">
    <xsl:param name="title" as="item()*"/>
    <xsl:param name="logo" as="item()*" required="false"/>
    <xsl:param name="link" as="xs:string?" required="false"/>

    <nav id="header" class="fixed w-full z-30 top-0 bg-gray-100 border-b border-gray-200">
      <div class="w-full container mx-auto flex flex-no-wrap items-center justify-between mt-0 py-3 md:px-4">
        <div class="flex-grow items-left hidden sm:block ">
          <xsl:choose>
            <xsl:when test="$title instance of xs:string">
              <h1 class="text-3xl font-bold">
                <a
                  class="toggleColour truncate text-gray-900
                        no-underline hover:no-underline"
                  href="{if ($link) then $link else '#'}">
                  <xsl:value-of select="$title"/>
                </a>
              </h1>
            </xsl:when>
            <xsl:otherwise>
              <h1
                class="toggleColour no-underline hover:no-underline text-gray-900 text-3xl font-bold">
                <xsl:copy-of select="$title"/>
              </h1>
            </xsl:otherwise>
          </xsl:choose>
        </div>
        <div class="block lg:hidden pr-4">
          <button id="nav-toggle"
                  class="flex items-center p-1 text-orange-800 hover:text-gray-900 focus:outline-none focus:shadow-outline transform transition hover:scale-105 duration-300 ease-in-out">
            <svg class="fill-current h-6 w-6" viewBox="0 0 20 20"
                 xmlns="http://www.w3.org/2000/svg">
              <title>Menu</title>
              <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"/>
            </svg>
          </button>
        </div>
        <div
          class="w-full flex-grow lg:flex lg:items-center lg:w-auto hidden lg:block mt-2 lg:mt-0 bg-white lg:bg-transparent text-black p-4 lg:p-0 z-20"
          id="nav-content">
          <ul class="list-reset lg:flex justify-end flex-1 items-center">
            <!--<li class="mr-3">
              <a class="inline-block py-2 px-4 text-black font-bold no-underline" href="#">Discover</a>
            </li>
            <li class="mr-3">
              <a class="inline-block text-black no-underline hover:text-gray-800 hover:text-underline py-2 px-4"
                 href="#">Map</a>
            </li>-->
          </ul>
          <!--<button
            id="navAction"
            class="bg-gray-800 text-white rounded
                   mx-auto lg:mx-0 mt-4 lg:mt-0 py-2 px-5 
                   focus:outline-none focus:shadow-outline
                   transform transition hover:scale-105 duration-300 ease-in-out"
          >
            Sign in
          </button>-->
        </div>
      </div>
    </nav>
  </xsl:template>


  <xsl:template name="html-breadcrumb">
    <xsl:param name="breadcrumb" select="''" as="item()*"/>
    <xsl:param name="header" select="''" as="item()*"/>

    <div class="pb-4">
      <div class="container mx-auto flex flex-wrap flex-col md:flex-row items-center z-30 md:px-4">
        <xsl:if test="$breadcrumb">
          <div
            class="flex flex-col w-full md:w-2/5 justify-center items-start text-center md:text-left">
            <p class="uppercase tracking-loose w-full">
              <xsl:copy-of select="$breadcrumb"/>
            </p>
          </div>
        </xsl:if>
        <xsl:if test="$header">
          <h2 class="text-2xl max-w-screen-lg">
            <xsl:copy-of select="$header"/>
          </h2>
        </xsl:if>
      </div>
    </div>
  </xsl:template>


  <xsl:template name="footer">
    <footer class="bg-white border-t border-gray-200">
      <div class="container mx-auto px-8 py-6 md:px-4">

        <a href="{$baseUrl}" class="float-left m-1">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M2 13.5V7h1v6.5a.5.5 0 0 0 .5.5h9a.5.5 0 0 0 .5-.5V7h1v6.5a1.5 1.5 0 0 1-1.5 1.5h-9A1.5 1.5 0 0 1 2 13.5zm11-11V6l-2-2V2.5a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5z"/>
            <path fill-rule="evenodd" d="M7.293 1.5a1 1 0 0 1 1.414 0l6.647 6.646a.5.5 0 0 1-.708.708L8 2.207 1.354 8.854a.5.5 0 1 1-.708-.708L7.293 1.5z"/>
          </svg>
        </a>
        |
        <a href="openapi">
          API
        </a>
        |
        <a href="https://geonetwork-opensource.org/" target="_blank">
          Powered by GeoNetwork
        </a>

      </div>
    </footer>
  </xsl:template>

</xsl:stylesheet>
