# frozen_string_literal: true

require 'rake/clean'
require 'asciidoctor'
require 'asciidoctor-pdf'
require 'fileutils'

# Configuration
BASE_DIR = 'docs'
SOURCE_DIR = 'stories'
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
  'imagesdir' => 'images',
  'pdf-themesdir' => PDF_THEME_DIR,
  'pdf-fontsdir' => PDF_FONTS_DIR,
  'pdf-theme' => 'el'
}.freeze

# Clean tasks
CLEAN.include("#{BUILD_DIR}/*.html", "#{BUILD_DIR}/*.pdf")
CLOBBER.include(BUILD_DIR, "#{BASE_DIR}/**/build")

# Helper method to convert a document to both HTML and PDF
def convert_document(source_file, output_folder)

  to_dir = output_folder + '/build'

  # Convert to HTML
  Asciidoctor.convert_file(
    source_file,
    safe: :unsafe,
    backend: 'html5',
    base_dir: BASE_DIR,
    to_dir: to_dir,
    mkdirs: true,
    attributes: COMMON_ATTRIBUTES
  )

  # Convert to PDF
  Asciidoctor.convert_file(
    source_file,
    safe: :unsafe,
    backend: 'pdf',
    base_dir: BASE_DIR,
    to_dir: to_dir,
    mkdirs: true,
    attributes: COMMON_ATTRIBUTES
  )
end

# Helper to copy files from a specific source build dir to target dir
def copy_files_to_build(source_pattern, target_dir)
  FileUtils.mkdir_p(target_dir)
  Dir.glob(source_pattern).each do |file|
    FileUtils.cp_r(file, target_dir, verbose: true, preserve: true)
  end
end

# Task to render chapter parts (must run first so PDFs are available for stories)
desc 'Render all chapter part files to PDF and HTML'
task :render_chapter_parts do
  chapter_dir = "#{BASE_DIR}/#{SOURCE_DIR}/chapter_parts"
  chapter_asciidoc_dir = "#{SOURCE_DIR}/chapter_parts"
  Dir.glob("#{chapter_dir}/*.adoc").each do |chapter_file|
    puts "Converting #{chapter_file}..."
    convert_document(chapter_file, chapter_asciidoc_dir)
  end
  puts "Copying chapter parts to #{BUILD_DIR}/stories/chapter_parts..."
  copy_files_to_build("#{chapter_dir}/build/.", "#{BUILD_DIR}/stories/chapter_parts/")
end

# Task to render all story files
desc 'Render all story files to PDF and HTML'
task :render_stories => :render_chapter_parts do
  Dir.glob("#{BASE_DIR}/#{SOURCE_DIR}/*.adoc").each do |story_file|
    puts "Converting #{story_file}..."
    convert_document(story_file, SOURCE_DIR)
  end
  puts "Copying stories to #{BUILD_DIR}/stories..."
  copy_files_to_build("#{BASE_DIR}/#{SOURCE_DIR}/build/.", "#{BUILD_DIR}/stories/")
end

# Task to render index
desc 'Render the index file to PDF and HTML'
task :render_index => :render_stories do
  index_file = "#{BASE_DIR}/index.adoc"
  if File.exist?(index_file)
    puts "Converting #{index_file}..."
    convert_document(index_file, ".")
  else
    puts "Warning: #{index_file} not found"
  end
  puts "Copying index to #{BUILD_DIR}..."
  copy_files_to_build("#{BASE_DIR}/build/.", "#{BUILD_DIR}/")
end

# Main build task
desc 'Build all story files (index, stories, and chapter parts)'
task :build => :render_index do
  puts "\nBuild completed successfully!"
  puts "Output directory: #{BUILD_DIR}"
end

# Default task
task default: :build
