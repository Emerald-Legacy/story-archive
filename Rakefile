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
  'imagesdir' => 'images',
  'pdf-themesdir' => PDF_THEME_DIR,
  'pdf-fontsdir' => PDF_FONTS_DIR,
  'pdf-theme' => 'el'
}.freeze

# Clean tasks
CLEAN.include("#{BUILD_DIR}/**/*.{html,pdf}")
CLOBBER.include(BUILD_DIR, "#{BASE_DIR}/**/build")

# Convert a single document to both HTML and PDF
def convert_document(source_file, output_dir)
  source_dir = File.dirname(source_file)
  absolute_output = File.expand_path(output_dir)

  %w[html5 pdf].each do |backend|
    Asciidoctor.convert_file(
      source_file,
      safe: :unsafe,
      backend: backend,
      base_dir: BASE_DIR,
      to_dir: absolute_output,
      mkdirs: true,
      attributes: COMMON_ATTRIBUTES
    )
  end
end

# Copy files from source to destination and clean up source
def copy_and_cleanup(source_dir, dest_dir)
  FileUtils.mkdir_p(dest_dir)
  FileUtils.cp_r("#{source_dir}/.", dest_dir)
  FileUtils.rm_rf(source_dir)
end

# Task to render chapter parts (must run first so PDFs are available for stories)
desc 'Render all chapter part files to PDF and HTML'
task :render_chapter_parts do
  chapter_dir = "#{SOURCE_DIR}/chapter_parts"
  build_dir = "#{chapter_dir}/build"

  Dir.glob("#{chapter_dir}/*.adoc").each do |chapter_file|
    puts "Converting #{chapter_file}..."
    convert_document(chapter_file, build_dir)
  end

  copy_and_cleanup(build_dir, "#{BUILD_DIR}/stories/chapter_parts")
end

# Task to render all story files
desc 'Render all story files to PDF and HTML'
task :render_stories => :render_chapter_parts do
  build_dir = "#{SOURCE_DIR}/build"

  Dir.glob("#{SOURCE_DIR}/*.adoc").each do |story_file|
    puts "Converting #{story_file}..."
    convert_document(story_file, build_dir)
  end

  copy_and_cleanup(build_dir, "#{BUILD_DIR}/stories")
end

# Task to render index
desc 'Render the index file to PDF and HTML'
task :render_index => :render_stories do
  index_file = "#{BASE_DIR}/index.adoc"

  if File.exist?(index_file)
    puts "Converting #{index_file}..."
    build_dir = "#{BASE_DIR}/build"
    convert_document(index_file, build_dir)
    copy_and_cleanup(build_dir, BUILD_DIR)
  else
    puts "Warning: #{index_file} not found"
  end
end

# Main build task
desc 'Build all story files (index, stories, and chapter parts)'
task :build => :render_index do
  puts "\nBuild completed successfully!"
  puts "Output directory: #{BUILD_DIR}"
end

# Default task
task default: :build
