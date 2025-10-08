# frozen_string_literal: true

require 'rake/clean'
require 'asciidoctor'
require 'asciidoctor-pdf'
require 'fileutils'

# Configuration
BASE_DIR = 'docs'
SOURCE_DIR = 'docs/stories'
BUILD_DIR = 'build'
PDF_THEME_DIR = 'pdf-theme/themes'
PDF_FONTS_DIR = 'pdf-theme/fonts'

# Common attributes for all documents
COMMON_ATTRIBUTES = {
  'icons' => 'font',
  'revnumber' => '',
  'revdate' => '',
  'version-label' => '',
  'language' => 'EN',
  'imagesdir' => '../images',
  'pdf-themesdir' => PDF_THEME_DIR,
  'pdf-fontsdir' => PDF_FONTS_DIR,
  'pdf-theme' => 'el'
}.freeze

# Clean tasks
CLEAN.include("#{BUILD_DIR}/*.html", "#{BUILD_DIR}/*.pdf")
CLOBBER.include(BUILD_DIR, "#{BASE_DIR}/**/build")

# Helper method to convert a document to both HTML and PDF
def convert_document(source_file, source_base_dir)
  # Convert to HTML
  Asciidoctor.convert_file(
    source_file,
    safe: :unsafe,
    backend: 'html5',
    base_dir: source_base_dir,
    to_dir: "build",
    mkdirs: true,
    attributes: COMMON_ATTRIBUTES
  )

  # Convert to PDF
  Asciidoctor.convert_file(
    source_file,
    safe: :unsafe,
    backend: 'pdf',
    base_dir: source_base_dir,
    to_dir: "build",
    mkdirs: true,
    attributes: COMMON_ATTRIBUTES
  )
end

# Helper to copy files from source build dirs to main build dir
def copy_to_build
  FileUtils.mkdir_p(BUILD_DIR)
  Dir.glob("#{BASE_DIR}/**/build/*.{html,pdf}").each do |file|
    FileUtils.cp(file, BUILD_DIR, verbose: true)
  end
end

# Task to render all story files
desc 'Render all story files to PDF and HTML'
task :render_stories do
  Dir.glob("#{SOURCE_DIR}/*.adoc").each do |story_file|
    puts "Converting #{story_file}..."
    convert_document(story_file, SOURCE_DIR)
  end
end

# Task to render chapter parts
desc 'Render all chapter part files to PDF and HTML'
task :render_chapter_parts do
  chapter_dir = "#{SOURCE_DIR}/chapter_parts"
  Dir.glob("#{chapter_dir}/*.adoc").each do |chapter_file|
    puts "Converting #{chapter_file}..."
    convert_document(chapter_file, chapter_dir)
  end
end

# Task to render index
desc 'Render the index file to PDF and HTML'
task :render_index do
  index_file = "#{BASE_DIR}/index.adoc"
  if File.exist?(index_file)
    puts "Converting #{index_file}..."
    convert_document(index_file, BASE_DIR)
  else
    puts "Warning: #{index_file} not found"
  end
end

# Main build task
desc 'Build all story files (index, stories, and chapter parts)'
task :build => [:render_stories, :render_chapter_parts, :render_index] do
  puts "\nCopying files to #{BUILD_DIR}..."
  copy_to_build
  puts "\nBuild completed successfully!"
  puts "Output directory: #{BUILD_DIR}"
end

# Default task
task default: :build
