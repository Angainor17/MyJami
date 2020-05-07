/*
 *  Copyright (C) 2004-2020 Savoir-faire Linux Inc.
 *
 *  Author: Philippe Gorley <philippe.gorley@savoirfairelinux.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA.
 */

#pragma once

#include "../../compat/msvc/config.h"
#include "media_buffer.h"
#include "media_encoder.h"
#include "media_filter.h"
#include "media_stream.h"
#include "noncopyable.h"
#include "observer.h"

#include <map>
#include <memory>
#include <mutex>
#include <queue>
#include <stdexcept>
#include <string>
#include <utility>

namespace jami {

class MediaRecorder : public std::enable_shared_from_this<MediaRecorder>
{
public:
    MediaRecorder();
    ~MediaRecorder();

    /**
     * @brief Gets whether or not the recorder is active.
     */
    bool isRecording() const;

    /**
     * @brief Get file path of file to be recorded.
     *
     * Same path as sent to @setPath, but with
     * file extension appended.
     *
     * NOTE @audioOnly must be called to have the right extension.
     */
    std::string getPath() const;

    /**
     * @brief Resulting file will be audio or video.
     *
     * Sets whether or not output file will have audio. Determines the extension of the
     * output file (.ogg or .webm).
     */
    void audioOnly(bool audioOnly);

    /**
     * @brief Sets output file path.
     *
     * NOTE An empty path will put the output file in the working directory.
     */
    void setPath(const std::string& path);

    /**
     * @brief Sets title and description metadata for the file.
     *
     * Uses default if either is empty.
     * Default title is "Conversation at %Y-%m-%d %H:%M:%S".
     * Default description is "Recorded with Jami https://jami.net".
     *
     * NOTE replaces %TIMESTAMP with time at start of recording
     */
    void setMetadata(const std::string& title, const std::string& desc);

    /**
     * @brief Adds a stream to the recorder.
     *
     * Caller must then attach this to the media source.
     */
    Observer<std::shared_ptr<MediaFrame>>* addStream(const MediaStream& ms);

    /**
     * @brief Gets the stream observer.
     *
     * This is so the caller can detach it from the media source.
     */
    Observer<std::shared_ptr<MediaFrame>>* getStream(const std::string& name) const;

    /**
     * @brief Initializes the file.
     *
     * Streams must have been added using Observable::attach and @addStream.
     */
    int startRecording();

    /**
     * @brief Finalizes the file.
     *
     * Streams must be removed using Observable::detach afterwards.
     */
    void stopRecording();

private:
    NON_COPYABLE(MediaRecorder);

    struct StreamObserver;

    void onFrame(const std::string& name, const std::shared_ptr<MediaFrame>& frame);

    void flush();
    void reset();

    int initRecord();
    MediaStream setupVideoOutput();
    std::string buildVideoFilter(const std::vector<MediaStream>& peers, const MediaStream& local) const;
    MediaStream setupAudioOutput();
    std::string buildAudioFilter(const std::vector<MediaStream>& peers, const MediaStream& local) const;

    std::mutex mutex_; // protect against concurrent file writes

    std::map<std::string, std::unique_ptr<StreamObserver>> streams_;

    std::string path_;
    std::tm startTime_;
    int64_t startTimeStamp_;
    std::string title_;
    std::string description_;

    std::unique_ptr<MediaEncoder> encoder_;
    std::unique_ptr<MediaFilter> videoFilter_;
    std::unique_ptr<MediaFilter> audioFilter_;

    bool hasAudio_ {false};
    bool hasVideo_ {false};
    int videoIdx_ = -1;
    int audioIdx_ = -1;
    bool isRecording_ = false;
    bool audioOnly_ = false;

    void filterAndEncode(MediaFilter* filter, int streamIdx);
};

}; // namespace jami
