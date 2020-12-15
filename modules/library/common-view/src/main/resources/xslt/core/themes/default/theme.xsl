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
      <link rel="stylesheet" href="https://unpkg.com/tailwindcss/dist/tailwind.min.css"/>
      <!--Replace with your tailwind.css once created-->
      <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700"
            rel="stylesheet"/>
      <style>
        .gradient {
        background: rgb(0,92,161);
        background: linear-gradient(126deg, rgba(0,92,161,1) 7%, rgba(202,201,0,1) 90%);
        }
      </style>
    </head>
  </xsl:template>


  <xsl:template name="html-body">
    <xsl:param name="title" as="item()*"/>
    <xsl:param name="logo" as="item()*" required="false"/>
    <xsl:param name="link" as="xs:string?" required="false"/>
    <xsl:param name="content" as="node()*"/>

    <body class="leading-normal tracking-normal text-white gradient">
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

    <nav id="header" class="fixed w-full z-30 top-0 text-white">
      <div class="w-full container mx-auto flex flex-no-wrap items-center justify-between mt-0 py-2">
        <xsl:if test="$logo">
          <div class="flex-shrink items-center w-10">
            <xsl:copy-of select="$logo"/>
          </div>
        </xsl:if>
        <div class="pl-4 flex-grow items-left hidden sm:block ">
          <xsl:choose>
            <xsl:when test="$title instance of xs:string">
              <a
                class="toggleColour text-white truncate
                       no-underline hover:no-underline font-bold
                       text-2xl lg:text-4xl"
                href="{if ($link) then $link else '#'}">
                <xsl:value-of select="$title"/>
              </a>
            </xsl:when>
            <xsl:otherwise>
              <div
                class="toggleColour text-white no-underline hover:no-underline font-bold text-2xl lg:text-4xl">
                <xsl:copy-of select="$title"/>
              </div>
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
          <button
            id="navAction"
            class="bg-white text-gray-800 font-bold rounded-full
                   mx-auto lg:mx-0 mt-4 lg:mt-0 py-4 px-8
                   shadow opacity-75
                   hover:underline
                   focus:outline-none focus:shadow-outline
                   transform transition hover:scale-105 duration-300 ease-in-out"
          >
            Sign in
          </button>
        </div>
      </div>
      <hr class="border-b border-gray-100 opacity-25 my-0 py-0"/>
    </nav>
  </xsl:template>


  <xsl:template name="html-breadcrumb">
    <xsl:param name="breadcrumb" select="''" as="item()*"/>
    <xsl:param name="header" select="''" as="item()*"/>

    <div class="pt-24 pb-4">
      <div class="container mx-auto flex flex-wrap flex-col md:flex-row items-center z-30">
        <xsl:if test="$breadcrumb">
          <div
            class="flex flex-col w-full md:w-2/5 justify-center items-start text-center md:text-left">
            <p class="uppercase tracking-loose w-full">
              <xsl:copy-of select="$breadcrumb"/>
            </p>
          </div>
        </xsl:if>
        <xsl:if test="$header">
          <xsl:copy-of select="$header"/>
        </xsl:if>
      </div>
    </div>
    <div class="relative -mt-12 lg:-mt-24"
         style="z-index:-1;">
      <svg viewBox="0 0 1428 174" version="1.1" xmlns="http://www.w3.org/2000/svg">
        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
          <g transform="translate(-2.000000, 44.000000)" fill="#FFFFFF" fill-rule="nonzero">
            <path
              d="M0,0 C90.7283404,0.927527913 147.912752,27.187927 291.910178,59.9119003 C387.908462,81.7278826 543.605069,89.334785 759,82.7326078 C469.336065,156.254352 216.336065,153.6679 0,74.9732496"
              opacity="0.100000001"></path>
            <path
              d="M100,104.708498 C277.413333,72.2345949 426.147877,52.5246657 546.203633,45.5787101 C666.259389,38.6327546 810.524845,41.7979068 979,55.0741668 C931.069965,56.122511 810.303266,74.8455141 616.699903,111.243176 C423.096539,147.640838 250.863238,145.462612 100,104.708498 Z"
              opacity="0.100000001"></path>
            <path
              d="M1046,51.6521276 C1130.83045,29.328812 1279.08318,17.607883 1439,40.1656806 L1439,120 C1271.17211,77.9435312 1140.17211,55.1609071 1046,51.6521276 Z"
              id="Path-4" opacity="0.200000003"></path>
          </g>
          <g transform="translate(-4.000000, 76.000000)" fill="#FFFFFF" fill-rule="nonzero">
            <path
              d="M0.457,34.035 C57.086,53.198 98.208,65.809 123.822,71.865 C181.454,85.495 234.295,90.29 272.033,93.459 C311.355,96.759 396.635,95.801 461.025,91.663 C486.76,90.01 518.727,86.372 556.926,80.752 C595.747,74.596 622.372,70.008 636.799,66.991 C663.913,61.324 712.501,49.503 727.605,46.128 C780.47,34.317 818.839,22.532 856.324,15.904 C922.689,4.169 955.676,2.522 1011.185,0.432 C1060.705,1.477 1097.39,3.129 1121.236,5.387 C1161.703,9.219 1208.621,17.821 1235.4,22.304 C1285.855,30.748 1354.351,47.432 1440.886,72.354 L1441.191,104.352 L1.121,104.031 L0.457,34.035 Z"></path>
          </g>
        </g>
      </svg>
    </div>
  </xsl:template>


  <xsl:template name="footer">
    <footer class="bg-white">
      <div class="container mx-auto px-8">
        <div class="w-full flex flex-col md:flex-row py-6">
          <div class="flex-1 mb-6 text-black">
            <a
              class="text-orange-600 no-underline hover:no-underline font-bold text-2xl lg:text-4xl"
              href="{$baseUrl}">/
            </a>
          </div>
          <div class="flex-1">
            <p class="uppercase text-gray-500 md:mb-6">Links</p>
            <ul class="list-reset mb-6">
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Help
                </a>
              </li>
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Support
                </a>
              </li>
            </ul>
          </div>
          <div class="flex-1">
            <p class="uppercase text-gray-500 md:mb-6">Legal</p>
            <ul class="list-reset mb-6">
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Terms
                </a>
              </li>
            </ul>
          </div>
          <div class="flex-1">
            <p class="uppercase text-gray-500 md:mb-6">Social</p>
            <ul class="list-reset mb-6">
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Facebook
                </a>
              </li>
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Linkedin
                </a>
              </li>
              <li class="mt-2 inline-block mr-2 md:block md:mr-0">
                <a href="#"
                   class="no-underline hover:underline text-gray-800 hover:text-orange-500">Twitter
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </footer>
  </xsl:template>

</xsl:stylesheet>